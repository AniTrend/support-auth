package co.anitrend.support.auth.twitter

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import co.anitrend.support.auth.core.CoreAuthActivity
import co.anitrend.support.auth.core.model.AuthCache
import co.anitrend.support.auth.core.view.DialogFactory
import co.anitrend.support.auth.twitter.presenter.TwitterPresenter
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.identity.TwitterAuthClient
import kotlinx.coroutines.cancel

internal class TwitterAuthActivity : CoreAuthActivity() {

    private var loadingDialog: ProgressDialog? = null

    private val twitterClient: TwitterAuthClient? by lazy(LazyThreadSafetyMode.NONE) {
        try {
            TwitterAuthClient()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private val twitterPresenter: TwitterPresenter? by lazy(LazyThreadSafetyMode.NONE) {
        TwitterPresenter(this, loadingDialog)
    }

    override val authenticationMeta by lazy {
        AuthCache.instance.twitterAuthenticationMeta
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadingDialog = DialogFactory.createLoadingDialog(this)

        val activeSession = TwitterCore.getInstance().sessionManager.activeSession

        when {
            activeSession != null ->
                twitterPresenter?.handleSuccess(
                    activeSession
                )
            else ->
                twitterClient?.authorize(
                    this,
                    twitterPresenter
                )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_CANCELED) {
            handleCancel()
            return
        }

        if (requestCode == twitterClient?.requestCode)
            twitterClient?.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleCancel() {
        twitterClient?.cancelAuthorize()
        super.onCancellation()
    }

    override fun onDestroy() {
        twitterPresenter?.cancel()
        super.onDestroy()
    }

    companion object {
        fun start(context: Context?) {
            val intent = Intent(context, TwitterAuthActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context?.startActivity(intent)
        }
    }
}
