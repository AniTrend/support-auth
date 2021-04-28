package co.anitrend.support.auth.facebook

import android.content.Context
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.facebook.login.LoginManager
import co.anitrend.support.auth.core.callback.AuthCallback
import co.anitrend.support.auth.core.callback.RevokeCallback
import co.anitrend.support.auth.core.model.AuthenticationMeta
import co.anitrend.support.auth.core.model.AuthCache
import co.anitrend.support.auth.core.ICoreAuth

object FacebookAuth : ICoreAuth {

    override fun connectToProvider(context: Context?, listener: AuthCallback, scopes: List<String>) {
        AuthCache.instance.facebookAuthenticationMeta = AuthenticationMeta(scopes, listener)
        FacebookAuthActivity.start(context)
    }

    override fun disconnectProvider(context: Context?) {
        AuthCache.instance.facebookAuthenticationMeta = null
        LoginManager.getInstance().logOut()
    }

    override fun revokeProvider(context: Context?, revokeCallback: RevokeCallback?) {
        GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/",
                null, HttpMethod.DELETE) {
            disconnectProvider(context)
            revokeCallback?.onRevoked()
        }.executeAsync()
    }
}

