package co.anitrend.support.auth.google

import android.content.Context
import co.anitrend.support.auth.core.callback.AuthCallback
import co.anitrend.support.auth.core.callback.RevokeCallback
import co.anitrend.support.auth.core.model.AuthenticationMeta
import co.anitrend.support.auth.core.model.AuthCache
import co.anitrend.support.auth.core.ICoreAuth

object GoogleAuth: ICoreAuth {

    override fun connectToProvider(context: Context?, listener: AuthCallback, scopes: List<String>) {
        AuthCache.instance.googleAuthenticationMeta = AuthenticationMeta(scopes, listener)
        GoogleAuthActivity.start(context)
    }

    override fun disconnectProvider(context: Context?) {
        AuthCache.instance.googleAuthenticationMeta = null
        GoogleAuthActivity.setGoogleDisconnectRequested(context, true)
    }

    override fun revokeProvider(context: Context?, revokeCallback: RevokeCallback?) {
        GoogleAuthActivity.setGoogleRevokeRequested(context, true)
    }
}
