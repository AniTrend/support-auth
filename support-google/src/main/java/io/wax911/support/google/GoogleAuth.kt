package io.wax911.support.google

import android.content.Context
import io.wax911.support.core.callback.AuthCallback
import io.wax911.support.core.callback.RevokeCallback
import io.wax911.support.core.model.AuthenticationMeta
import io.wax911.support.core.model.AuthStorage
import io.wax911.support.core.ICoreAuth

object GoogleAuth: ICoreAuth {

    override fun connectToProvider(context: Context?, listener: AuthCallback, scopes: List<String>) {
        AuthStorage.instance.googleAuthenticationMeta = AuthenticationMeta(scopes, listener)
        GoogleAuthActivity.start(context)
    }

    override fun disconnectProvider(context: Context?) {
        AuthStorage.instance.googleAuthenticationMeta = null
        GoogleAuthActivity.setGoogleDisconnectRequested(context, true)
    }

    override fun revokeProvider(context: Context?, revokeCallback: RevokeCallback?) {
        GoogleAuthActivity.setGoogleRevokeRequested(context, true)
    }
}
