package io.wax911.support.facebook

import android.content.Context
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.facebook.login.LoginManager
import io.wax911.support.core.callback.AuthCallback
import io.wax911.support.core.callback.RevokeCallback
import io.wax911.support.core.model.AuthenticationMeta
import io.wax911.support.core.model.AuthStorage
import io.wax911.support.core.ICoreAuth

object FacebookAuth : ICoreAuth {

    override fun connectToProvider(context: Context?, listener: AuthCallback, scopes: List<String>) {
        AuthStorage.instance.facebookAuthenticationMeta = AuthenticationMeta(scopes, listener)
        FacebookAuthActivity.start(context)
    }

    override fun disconnectProvider(context: Context?) {
        AuthStorage.instance.facebookAuthenticationMeta = null
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

