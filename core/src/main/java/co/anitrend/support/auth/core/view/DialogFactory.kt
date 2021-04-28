package co.anitrend.support.auth.core.view

import android.app.ProgressDialog
import android.content.Context
import co.anitrend.support.auth.core.R

object DialogFactory {

    fun createLoadingDialog(context: Context): ProgressDialog {
        val loadingDialog = ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT)
        loadingDialog.setCancelable(false)
        loadingDialog.setCanceledOnTouchOutside(false)
        loadingDialog.setMessage(context.getString(R.string.text_loading))
        return loadingDialog
    }
}
