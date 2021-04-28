import co.anitrend.support.auth.buildSrc.Libraries

plugins {
    id("co.anitrend.support.auth")
}

android {
    buildTypes.getByName("debug") {
        manifestPlaceholders.putIfAbsent(
            "googleWebClientId", "49247361267-bm9eqtf4nvkchua8rq75q9a3qolf9ng0.apps.googleusercontent.com"
        )
    }
}

dependencies {

    implementation(project(":core"))
    implementation(Libraries.Google.Services.Auth.auth)
}