package io.wax911.support.core.model

class AuthStorage private constructor() {

    var facebookAuthenticationMeta: AuthenticationMeta? = null
    var googleAuthenticationMeta: AuthenticationMeta? = null
    var twitterAuthenticationMeta: AuthenticationMeta? = null

    companion object {
        val instance by lazy { AuthStorage() }
    }
}
