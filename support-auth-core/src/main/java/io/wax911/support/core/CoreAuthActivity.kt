package io.wax911.support.core

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import io.wax911.support.core.model.AuthenticationMeta
import io.wax911.support.core.model.SocialUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class CoreAuthActivity : AppCompatActivity() {

    protected abstract val authenticationMeta: AuthenticationMeta?
    private lateinit var job: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        job = Job()
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
    }


    protected fun onCancellation() {
        if (lifecycle.currentState != Lifecycle.State.DESTROYED) {
            authenticationMeta?.callback?.onCancel()
            finish()
        }
    }

    protected fun onExceptionThrown(error: Throwable) {
        if (lifecycle.currentState != Lifecycle.State.DESTROYED) {
            authenticationMeta?.callback?.onError(error)
            finish()
        }
    }


    protected fun onSocialSuccess(user: SocialUser) {
        if (lifecycle.currentState != Lifecycle.State.DESTROYED) {
            authenticationMeta?.callback?.onSuccess(user)
            finish()
        }
    }

    override fun onDestroy() {
        job.cancel()
        authenticationMeta?.clearCallback()
        super.onDestroy()
    }

    /**
     * @return Context of this scope.
     */
    val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default
}
