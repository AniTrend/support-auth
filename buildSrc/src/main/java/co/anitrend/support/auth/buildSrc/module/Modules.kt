package co.anitrend.support.auth.buildSrc.module

internal object Modules {

    interface Module {
        val id: String

        /**
         * @return Formatted id of module as a path string
         */
        fun path(): String = ":$id"
    }

    enum class Sample(override val id: String) : Module {
        App("app")
    }

    enum class Support(override val id: String) : Module {
        Core("core"),
        Facebook("facebook"),
        Google("google"),
        Twitter("twitter")
    }
}