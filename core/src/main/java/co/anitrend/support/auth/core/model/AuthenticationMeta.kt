package co.anitrend.support.auth.core.model

import co.anitrend.support.auth.core.callback.AuthCallback

class AuthenticationMeta(var scopes: List<String>, var callback: AuthCallback?) {

    internal fun clearCallback() {
        callback = null
    }
}
