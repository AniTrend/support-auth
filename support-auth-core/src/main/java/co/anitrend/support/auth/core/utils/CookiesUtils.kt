package co.anitrend.support.auth.core.utils

import android.content.Context
import android.os.Build
import android.webkit.CookieManager
import android.webkit.CookieSyncManager

object CookiesUtils {

    fun clearCookies(context: Context?) {
        if (Build.VERSION.SDK_INT >= 21) {
            CookieManager.getInstance().removeAllCookies(null)
        } else {
            context?.apply {
                CookieSyncManager.createInstance(applicationContext)
                CookieManager.getInstance().removeAllCookie()
            }
        }
    }
}
