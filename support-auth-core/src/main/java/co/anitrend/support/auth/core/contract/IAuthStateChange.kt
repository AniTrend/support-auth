package co.anitrend.support.auth.core.contract

import co.anitrend.support.auth.core.model.SocialUser

interface IAuthStateChange {

    fun onCancellation()

    fun onExceptionThrown(error: Throwable?)

    fun onSocialSuccess(user: SocialUser?)
}