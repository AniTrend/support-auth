package io.wax911.support.core.view

import android.app.ProgressDialog
import android.content.Context
import io.wax911.support.common.R

object DialogFactory {

    fun createLoadingDialog(context: Context): ProgressDialog {
        val loadingDialog = ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT)
        loadingDialog.setCancelable(false)
        loadingDialog.setCanceledOnTouchOutside(false)
        loadingDialog.setMessage(context.getString(R.string.text_loading))
        return loadingDialog
    }
}
