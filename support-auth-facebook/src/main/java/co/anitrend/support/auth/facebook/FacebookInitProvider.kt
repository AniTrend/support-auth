package co.anitrend.support.auth.facebook

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.facebook.FacebookSdk
import co.anitrend.support.auth.core.extension.getMetaValue

class FacebookInitProvider : ContentProvider() {

  private fun initFacebook() {
    val facebookAppId = context?.getMetaValue(R.string.co_anitrend_support_auth_facebookId)
    if (facebookAppId != null && facebookAppId.isNotEmpty()) {
      FacebookSdk.setApplicationId(facebookAppId)
      FacebookSdk.sdkInitialize(context)
      FacebookSdk.setWebDialogTheme(android.R.style.Theme_Holo_Light_NoActionBar)
    }
  }

  override fun onCreate(): Boolean {
    initFacebook()
    return false
  }

  override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
    return null
  }

  override fun getType(uri: Uri): String? {
    return null
  }

  override fun insert(uri: Uri, values: ContentValues?): Uri? {
    return null
  }

  override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
    return 0
  }

  override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
    return 0
  }

}
