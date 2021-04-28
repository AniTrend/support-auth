import co.anitrend.support.auth.buildSrc.Libraries

plugins {
    id("co.anitrend.support.auth")
}

android {
    buildTypes.getByName("debug") {
        manifestPlaceholders.putIfAbsent(
            "facebookAppId", "189953571452011"
        )
    }
}

dependencies {

    implementation(project(":core"))
    implementation(Libraries.Facebook.androidSdk)
}