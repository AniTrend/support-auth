package co.anitrend.support.auth.twitter

import android.content.Context
import android.util.Log
import com.twitter.sdk.android.core.TwitterCore
import co.anitrend.support.auth.core.callback.AuthCallback
import co.anitrend.support.auth.core.callback.RevokeCallback
import co.anitrend.support.auth.core.model.AuthenticationMeta
import co.anitrend.support.auth.core.model.AuthCache
import co.anitrend.support.auth.core.utils.CookiesUtils
import co.anitrend.support.auth.core.ICoreAuth
import timber.log.Timber

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
        Timber.e("Twitter does not have this functionality")
    }
}
