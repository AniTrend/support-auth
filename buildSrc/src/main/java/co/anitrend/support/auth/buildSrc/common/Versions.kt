package co.anitrend.support.auth.buildSrc.common

object Versions {

    private fun Int.toVersion(): String {
        return if (this < 9) "0$this" else "$this"
    }

    const val compileSdk = 30
    const val targetSdk = 30
    const val minSdk = 17

    private const val major = 0
    private const val minor = 5
    private const val patch = 2
    private const val revision = 1

    const val versionCode = major * 100_000 + minor * 10_000 + patch * 1_000 + revision * 100
    val versionName = "$major.$minor.$patch-alpha${revision.toVersion()}"

    const val mockk = "1.11.0"
    const val junit = "4.13.2"

    const val timber = "4.7.1"
    const val ktlint = "0.40.0"
}
