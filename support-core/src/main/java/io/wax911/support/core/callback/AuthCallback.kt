package io.wax911.support.core.callback

import io.wax911.support.core.model.SocialUser

interface AuthCallback {
    fun onSuccess(socialUser: SocialUser)

    fun onError(error: Throwable)

    fun onCancel()
}
