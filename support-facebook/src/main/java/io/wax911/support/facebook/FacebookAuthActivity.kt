package io.wax911.support.facebook

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
import io.wax911.support.core.model.AuthenticationMeta
import io.wax911.support.core.model.AuthStorage
import io.wax911.support.core.view.DialogFactory
import io.wax911.support.core.CoreAuthActivity
import io.wax911.support.core.model.SocialUser
import io.wax911.support.core.extension.isFacebookInstalled

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
        AuthStorage.instance.facebookAuthenticationMeta
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
        val user = SocialUser()
        user.userId = `object`.optString("id", "")
        user.accessToken = AccessToken.getCurrentAccessToken().token
        user.profilePictureUrl = String.format(PROFILE_PIC_URL, user.userId)
        user.email = `object`.optString("email", "")
        user.fullName = `object`.optString("name", "")
        user.pageLink = `object`.optString("link", "")
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
