package co.anitrend.support.auth.twitter

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.identity.TwitterAuthClient
import co.anitrend.support.auth.core.model.AuthCache
import co.anitrend.support.auth.core.model.SocialUser
import co.anitrend.support.auth.core.view.DialogFactory
import co.anitrend.support.auth.core.CoreAuthActivity
import kotlinx.coroutines.*

class TwitterAuthActivity : CoreAuthActivity() {

    private val twitterClient: TwitterAuthClient? by lazy {
        try {
            TwitterAuthClient()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private val callback = object : Callback<TwitterSession>() {
        override fun success(result: Result<TwitterSession>) {
            handleSuccess(result.data)
        }

        override fun failure(exception: TwitterException) {
            onExceptionThrown(exception)
        }
    }

    override val authenticationMeta by lazy {
        AuthCache.instance.twitterAuthenticationMeta
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activeSession = TwitterCore.getInstance().sessionManager.activeSession
        when {
            activeSession != null -> handleSuccess(activeSession)
            else -> twitterClient?.authorize(this, callback)
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

    private fun handleSuccess(session: TwitterSession) {
        val loadingDialog = DialogFactory
            .createLoadingDialog(this)
            .also { it.show() }

        launch (Dispatchers.IO) {
            try {
                val twitterApiClient = TwitterCore.getInstance().apiClient
                val accountService = twitterApiClient.accountService
                val call = accountService.verifyCredentials(
                    false, true, true
                )
                val response = call.execute()

                dismissLoadingDialog(loadingDialog)

                val user = SocialUser().apply {
                    response.body()?.also {
                        userId = it.getId().toString()
                        accessToken = session.authToken.token
                        secretToken = session.authToken.secret
                        profilePictureUrl = String.format(PROFILE_PIC_URL, it.screenName)
                        email = it.email
                        fullName = it.name
                        username = it.screenName
                        pageLink = String.format(PAGE_LINK, it.screenName)
                    }
                }
                withContext(Dispatchers.Main) { onSocialSuccess(user) }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    onExceptionThrown(e)
                    dismissLoadingDialog(loadingDialog)
                }
            }
        }
    }

    private suspend fun dismissLoadingDialog(progressDialog: ProgressDialog) = withContext(Dispatchers.Main) {
        progressDialog.dismiss()
    }

    private fun handleCancel() {
        twitterClient?.cancelAuthorize()
        super.onCancellation()
    }

    companion object {

        private const val PROFILE_PIC_URL = "https://twitter.com/%1\$s/profile_image?size=original"
        private const val PAGE_LINK = "https://twitter.com/%1\$s"

        fun start(context: Context?) {
            val intent = Intent(context, TwitterAuthActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context?.startActivity(intent)
        }
    }
}
