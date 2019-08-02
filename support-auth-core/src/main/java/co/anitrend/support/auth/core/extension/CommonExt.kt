package co.anitrend.support.auth.core.extension

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.annotation.StringRes

fun Context.getMetaValue(@StringRes stringRes: Int): String? {
    return try {
        val name = getString(stringRes)
        val applicationInfo = packageManager.getApplicationInfo(
            packageName,
            PackageManager.GET_META_DATA
        )
        (applicationInfo.metaData.get(name) as String?)
            ?.trim { it <= ' ' }
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        null
    }
}

fun Context.isFacebookInstalled(): Boolean {
    val facebook = applicationContext.packageManager
            .getLaunchIntentForPackage("com.facebook.katana")
    return facebook != null
}

fun Context.isTwitterInstalled(): Boolean {
    val facebook = applicationContext.packageManager
            .getLaunchIntentForPackage("com.twitter.android")
    return facebook != null
}