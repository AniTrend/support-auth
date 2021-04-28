package co.anitrend.support.auth.twitter.presenter

import android.app.ProgressDialog
import co.anitrend.support.auth.core.contract.IAuthStateChange
import co.anitrend.support.auth.core.model.SocialUser
import com.twitter.sdk.android.core.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

internal class TwitterPresenter(
    private val authStateChange: IAuthStateChange?,
    private val loadingDialog: ProgressDialog?
) : Callback<TwitterSession>(), CoroutineScope {

    private val supervisorJob: Job = SupervisorJob()

    fun handleSuccess(session: TwitterSession?) {
        loadingDialog?.show()

        launch (Dispatchers.IO) {
            try {
                val twitterApiClient = TwitterCore.getInstance().apiClient
                val accountService = twitterApiClient.accountService
                val call = accountService.verifyCredentials(
                    false, true, true
                )
                val response = call.execute()

                dismissLoadingDialog()

                val user = response.body()?.let {
                    SocialUser(
                        userId = it.getId().toString(),
                        accessToken = session?.authToken?.token,
                        secretToken = session?.authToken?.secret,
                        profilePictureUrl = String.format(PROFILE_PIC_URL, it.screenName),
                        email = it.email,
                        fullName = it.name,
                        username = it.screenName,
                        pageLink = String.format(PAGE_LINK, it.screenName)
                    )
                }

                withContext(Dispatchers.Main) {
                    authStateChange?.onSocialSuccess(user)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    authStateChange?.onExceptionThrown(e)
                    dismissLoadingDialog()
                }
            }
        }
    }

    private suspend fun dismissLoadingDialog() = withContext(Dispatchers.Main) {
        loadingDialog?.dismiss()
    }

    /**
     * Called when call completes successfully.
     *
     * @param result the parsed result.
     */
    override fun success(result: Result<TwitterSession>?) {
        handleSuccess(result?.data)
    }

    /**
     * Unsuccessful call due to network failure, non-2XX status code, or unexpected
     * exception.
     */
    override fun failure(exception: TwitterException?) {
        authStateChange?.onExceptionThrown(exception)
    }

    /**
     * The context of this scope.
     * Context is encapsulated by the scope and used for implementation of coroutine builders that are extensions on the scope.
     * Accessing this property in general code is not recommended for any purposes except accessing the [Job] instance for advanced usages.
     *
     * By convention, should contain an instance of a [job][Job] to enforce structured concurrency.
     */
    override val coroutineContext: CoroutineContext
        get() = supervisorJob + Dispatchers.IO

    companion object {
        private const val PROFILE_PIC_URL = "https://twitter.com/%1\$s/profile_image?size=original"
        private const val PAGE_LINK = "https://twitter.com/%1\$s"
    }
}