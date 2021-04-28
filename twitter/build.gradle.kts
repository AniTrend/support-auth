import co.anitrend.support.auth.buildSrc.Libraries

plugins {
    id("co.anitrend.support.auth")
}

android {
    buildTypes.getByName("debug") {
        manifestPlaceholders.putIfAbsent(
            "twitterConsumerKey", "K25JWbAcXzGx8yCAjqydgCGtt"
        )
        manifestPlaceholders.putIfAbsent(
            "twitterConsumerSecret", "t91O95O0ULFXrSQOHTHpWwqYpvvcFAwgVX4vC1yCdSnRKxV56F"
        )
    }
}

dependencies {
    implementation(project(":core"))
    implementation(Libraries.Twitter.core)
}