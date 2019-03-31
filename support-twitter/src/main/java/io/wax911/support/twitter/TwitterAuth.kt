package io.wax911.support.twitter

import android.content.Context
import android.util.Log
import com.twitter.sdk.android.core.TwitterCore
import io.wax911.support.core.callback.AuthCallback
import io.wax911.support.core.callback.RevokeCallback
import io.wax911.support.core.model.AuthenticationMeta
import io.wax911.support.core.model.AuthCache
import io.wax911.support.core.utils.CookiesUtils
import io.wax911.support.core.ICoreAuth

object TwitterAuth: ICoreAuth {

    override fun connectToProvider(context: Context?, listener: AuthCallback, scopes: List<String>) {
        AuthCache.instance.twitterAuthenticationMeta = AuthenticationMeta(scopes, listener)
        TwitterAuthActivity.start(context)
    }

    override fun disconnectProvider(context: Context?) {
        AuthCache.instance.twitterAuthenticationMeta = null
        TwitterCore.getInstance().sessionManager.clearActiveSession()
        CookiesUtils.clearCookies(context)
    }

    override fun revokeProvider(context: Context?, revokeCallback: RevokeCallback?) {
        Log.e(toString(), "Twitter does not have this functionality")
    }
}
