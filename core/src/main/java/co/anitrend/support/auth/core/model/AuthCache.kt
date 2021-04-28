package co.anitrend.support.auth.core.model

class AuthCache private constructor() {

    var facebookAuthenticationMeta: AuthenticationMeta? = null
    var googleAuthenticationMeta: AuthenticationMeta? = null
    var twitterAuthenticationMeta: AuthenticationMeta? = null

    companion object {
        val instance by lazy { AuthCache() }
    }
}
