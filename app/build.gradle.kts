plugins {
    id("co.anitrend.support.auth")
}

android {
    buildTypes.getByName("release") {
        manifestPlaceholders.putAll(
            mapOf(
                "facebookAppId" to "your facebook app id",
                "googleWebClientId" to "your google web client id",
                "twitterConsumerKey" to "your twitter consumer key",
                "twitterConsumerSecret" to "your twitter consumer secret"
            )
        )
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":facebook"))
    implementation(project(":twitter"))
    implementation(project(":google"))
}
