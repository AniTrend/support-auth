package co.anitrend.support.auth.twitter

import android.content.Context
import co.anitrend.support.auth.core.extension.getMetaValue
import co.anitrend.support.auth.core.provider.AuthInitProvider
import com.twitter.sdk.android.core.Twitter
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.twitter.sdk.android.core.TwitterConfig

internal class TwitterInitProvider : AuthInitProvider() {

    private fun initTwitter(context: Context?) {
        val consumerKey = context?.getMetaValue(R.string.co_anitrend_support_auth_twitterConsumerKey)
        val consumerSecret = context?.getMetaValue(R.string.co_anitrend_support_auth_twitterConsumerSecret)
        if (consumerKey != null && consumerKey.isNotEmpty() && consumerSecret != null && consumerSecret.isNotEmpty()) {
            val twitterConfig = TwitterConfig.Builder(context)
            .twitterAuthConfig(TwitterAuthConfig(consumerKey, consumerSecret))
            .build()
            Twitter.initialize(twitterConfig)
        }
    }

    override fun onCreate(): Boolean {
        initTwitter(context?.applicationContext)
        return false
    }
}
