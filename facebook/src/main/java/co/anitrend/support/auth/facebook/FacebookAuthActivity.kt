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
import co.anitrend.support.auth.facebook.presenter.FacebookPresenter

internal class FacebookAuthActivity : CoreAuthActivity() {

    override val authenticationMeta by lazy {
        AuthCache.instance.facebookAuthenticationMeta
    }

    private var callbackManager: CallbackManager? = null
    private var loadingDialog: ProgressDialog? = null

    private val scopes = listOf("email", "public_profile")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        callbackManager = CallbackManager.Factory.create()
        loadingDialog = DialogFactory.createLoadingDialog(this)
        val facebookPresenter = FacebookPresenter(this, loadingDialog)

        if (isFacebookInstalled())
            LoginManager.getInstance().logOut()

        LoginManager.getInstance().registerCallback(callbackManager, facebookPresenter)

        LoginManager.getInstance().logInWithReadPermissions(this, scopes)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }

    companion object {

        fun start(context: Context?) {
            val intent = Intent(context, FacebookAuthActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context?.startActivity(intent)
        }
    }
}
