package co.anitrend.support.auth.core

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import co.anitrend.support.auth.core.model.AuthenticationMeta
import co.anitrend.support.auth.core.model.SocialUser
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class CoreAuthActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    protected abstract val authenticationMeta: AuthenticationMeta?

    override fun onCreate(savedInstanceState: Bundle?) {
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
        cancel()
        authenticationMeta?.clearCallback()
        super.onDestroy()
    }
}
