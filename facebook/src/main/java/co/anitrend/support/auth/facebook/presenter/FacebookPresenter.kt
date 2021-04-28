package co.anitrend.support.auth.facebook.presenter

import android.app.ProgressDialog
import android.os.Bundle
import co.anitrend.support.auth.core.contract.IAuthStateChange
import co.anitrend.support.auth.core.model.SocialUser
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import org.json.JSONObject

internal class FacebookPresenter(
    private val authStateChange: IAuthStateChange?,
    private val loadingDialog: ProgressDialog?
) : FacebookCallback<LoginResult>, GraphRequest.GraphJSONObjectCallback {

    /**
     * Called when the dialog completes without error.
     *
     * Note: This will be called instead of [.onCancel] if any of the following conditions
     * are true.
     *
     * [com.facebook.share.widget.MessageDialog] is used.
     *
     * The logged in Facebook user has not authorized the app that has initiated the dialog.
     *
     * @param result Result from the dialog
     */
    override fun onSuccess(result: LoginResult?) {
        loadingDialog?.show()
        val request = GraphRequest.newMeRequest(
            result?.accessToken, this
        )
        with (request) {
            parameters = Bundle().apply {
                putString("fields", "id,name,email,link")
            }
            executeAsync()
        }
    }

    /**
     * Called when the dialog is canceled.
     *
     * Note: [.onSuccess] will be called instead if any of the following conditions
     * are true.
     *
     * [com.facebook.share.widget.MessageDialog] is used.
     *
     * The logged in Facebook user has not authorized the app that has initiated the dialog.
     */
    override fun onCancel() {
        authStateChange?.onCancellation()
    }

    /**
     * Called when the dialog finishes with an error.
     *
     * @param error The error that occurred
     */
    override fun onError(error: FacebookException?) {
        if (error is FacebookAuthorizationException)
            LoginManager.getInstance().logOut()
        authStateChange?.onExceptionThrown(error)
    }

    /**
     * The method that will be called when the request completes.
     *
     * @param object   the GraphObject representing the returned object, or null
     * @param response the Response of this request, which may include error information if the
     * request was unsuccessful
     */
    override fun onCompleted(`object`: JSONObject?, response: GraphResponse?) {
        if (response?.error != null) {
            `object`?.apply {
                val userId = optString("id", "")
                val user = SocialUser(
                    userId = userId,
                    accessToken = AccessToken.getCurrentAccessToken().token,
                    profilePictureUrl = String.format(PROFILE_PIC_URL, userId),
                    email = optString("email", ""),
                    fullName = optString("name", ""),
                    pageLink = optString("link", "")
                )
                authStateChange?.onSocialSuccess(user)
            }
        } else
            authStateChange?.onExceptionThrown(response?.error?.exception)
        loadingDialog?.dismiss()
    }

    companion object {
        private const val PROFILE_PIC_URL = "https://graph.facebook.com/%1\$s/picture?type=large"
    }
}