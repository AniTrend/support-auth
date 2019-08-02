package co.anitrend.support.auth.core

import android.content.Context
import co.anitrend.support.auth.core.callback.AuthCallback
import co.anitrend.support.auth.core.callback.RevokeCallback

interface ICoreAuth {

    fun connectToProvider(context: Context?, listener: AuthCallback, scopes: List<String> = listOf())
    fun disconnectProvider(context: Context?)
    fun revokeProvider(context: Context?, revokeCallback: RevokeCallback? = null)
}