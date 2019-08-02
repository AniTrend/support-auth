package co.anitrend.support.auth.facebook

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle

import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookAuthorizationException
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.GraphResponse
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult

import org.json.JSONObject

import java.util.Arrays
import co.anitrend.support.auth.core.model.AuthCache
import co.anitrend.support.auth.core.view.DialogFactory
import co.anitrend.support.auth.core.CoreAuthActivity
import co.anitrend.support.auth.core.model.SocialUser
import co.anitrend.support.auth.core.extension.isFacebookInstalled

class FacebookAuthActivity : CoreAuthActivity(), FacebookCallback<LoginResult>, GraphRequest.GraphJSONObjectCallback {

    private var callbackManager: CallbackManager? = null
    private var loadingDialog: ProgressDialog? = null

    private val scopes: List<String> by lazy {
        Arrays.asList("email", "public_profile")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadingDialog = DialogFactory.createLoadingDialog(this)

        callbackManager = CallbackManager.Factory.create()

        if (isFacebookInstalled())
            LoginManager.getInstance().logOut()

        LoginManager.getInstance().registerCallback(callbackManager, this)

        LoginManager.getInstance().logInWithReadPermissions(this, scopes)
    }

    override val authenticationMeta by lazy {
        AuthCache.instance.facebookAuthenticationMeta
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onSuccess(loginResult: LoginResult) {
        loadingDialog?.show()
        val request = GraphRequest.newMeRequest(loginResult.accessToken, this)
        val parameters = Bundle()
        parameters.putString("fields", "id,name,email,link")
        request.parameters = parameters
        request.executeAsync()
    }

    override fun onCancel() {
        onCancellation()
    }

    override fun onError(error: FacebookException) {
        onExceptionThrown(error)
        if (error is FacebookAuthorizationException) {
            LoginManager.getInstance().logOut()
        }
    }

    override fun onCompleted(`object`: JSONObject, response: GraphResponse) {
        val user = SocialUser().apply {
            userId = `object`.optString("id", "")
            accessToken = AccessToken.getCurrentAccessToken().token
            profilePictureUrl = String.format(PROFILE_PIC_URL, userId)
            email = `object`.optString("email", "")
            fullName = `object`.optString("name", "")
            pageLink = `object`.optString("link", "")
        }
        loadingDialog?.dismiss()
        onSocialSuccess(user)
    }

    companion object {

        private const val PROFILE_PIC_URL = "https://graph.facebook.com/%1\$s/picture?type=large"

        fun start(context: Context?) {
            val intent = Intent(context, FacebookAuthActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context?.startActivity(intent)
        }
    }
}
