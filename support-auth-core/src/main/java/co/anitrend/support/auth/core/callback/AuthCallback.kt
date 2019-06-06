package co.anitrend.support.auth.core.callback

import co.anitrend.support.auth.core.model.SocialUser

interface AuthCallback {
    fun onSuccess(socialUser: SocialUser)

    fun onError(error: Throwable)

    fun onCancel()
}
