package io.wax911.support.core

import android.content.Context
import io.wax911.support.core.callback.AuthCallback
import io.wax911.support.core.callback.RevokeCallback

interface ICoreAuth {

    fun connectToProvider(context: Context?, listener: AuthCallback, scopes: List<String> = listOf())
    fun disconnectProvider(context: Context?)
    fun revokeProvider(context: Context?, revokeCallback: RevokeCallback? = null)
}