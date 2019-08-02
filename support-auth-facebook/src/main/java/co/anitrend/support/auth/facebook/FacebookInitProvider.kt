package co.anitrend.support.auth.facebook

import co.anitrend.support.auth.core.extension.getMetaValue
import co.anitrend.support.auth.core.provider.AuthInitProvider
import com.facebook.FacebookSdk

internal class FacebookInitProvider : AuthInitProvider() {

    private fun initFacebook() {
        val facebookAppId = context?.getMetaValue(R.string.co_anitrend_support_auth_facebookId)
        if (facebookAppId != null && facebookAppId.isNotEmpty()) {
            FacebookSdk.setApplicationId(facebookAppId)
        }
    }

    override fun onCreate(): Boolean {
        initFacebook()
        return false
    }
}
