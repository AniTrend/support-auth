package io.wax911.support.core.model

import io.wax911.support.core.callback.AuthCallback

class AuthenticationMeta(var scopes: List<String>, var callback: AuthCallback?) {

    internal fun clearCallback() {
        callback = null
    }
}
