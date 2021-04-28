package co.anitrend.support.auth.buildSrc

import co.anitrend.support.auth.buildSrc.common.Versions

object Libraries {
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"

    const val junit = "junit:junit:${Versions.junit}"
    const val mockk = "io.mockk:mockk:${Versions.mockk}"

    object Android {

        object Tools {
            private const val version = "4.1.3"
            const val buildGradle = "com.android.tools.build:gradle:${version}"
        }
    }

    object AndroidX {

        object Core {
            private const val version = "1.5.0-rc01"
            const val core = "androidx.core:core:$version"
            const val coreKtx = "androidx.core:core-ktx:$version"
        }

        object Lifecycle {
            private const val version = "2.3.0"
            const val extensions = "androidx.lifecycle:lifecycle-extensions:2.2.0"
            const val runTimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
            const val liveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:$version"
            const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
            const val liveDataCoreKtx = "androidx.lifecycle:lifecycle-livedata-core-ktx:$version"
        }

        object StartUp {
            private const val version = "1.0.0"
            const val startUpRuntime = "androidx.startup:startup-runtime:$version"
        }

        object Test {
            private const val version = "1.3.0"
            const val core = "androidx.test:core:$version"
            const val coreKtx = "androidx.test:core-ktx:$version"
            const val runner = "androidx.test:runner:$version"
            const val rules = "androidx.test:rules:$version"

            object Extension {
                private const val version = "1.1.2"
                const val junit = "androidx.test.ext:junit:$version"
                const val junitKtx = "androidx.test.ext:junit-ktx:$version"
            }
        }
    }

    object CashApp {
        object Turbine {
            private const val version = "0.4.1"
            const val turbine = "app.cash.turbine:turbine:$version"
        }
    }

    object Facebook {
        private const val version = "9.1.1"
        const val androidSdk = "com.facebook.android:facebook-android-sdk:$version"
    }

    object Google {

        object Material {
            private const val version = "1.3.0"
            const val material = "com.google.android.material:material:$version"
        }

        object Services {
            object Auth {
                private const val version = "19.0.0"
                const val auth = "com.google.android.gms:play-services-auth:$version"
            }
        }
    }

    object JetBrains {
        object Dokka {
            private const val version = "1.4.30"
            const val gradlePlugin = "org.jetbrains.dokka:dokka-gradle-plugin:$version"
        }

        object Kotlin {
            private const val version = "1.4.32"
            const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
            const val reflect = "org.jetbrains.kotlin:kotlin-reflect:$version"

            object Gradle {
                const val plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
            }

            object Android {
                const val extensions = "org.jetbrains.kotlin:kotlin-android-extensions:$version"
            }
        }

        object KotlinX {
            object Coroutines {
                private const val version = "1.4.3"
                const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
                const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
                const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
            }
        }
    }

    object Twitter {
        private const val version = "3.3.0"
        const val core = "com.twitter.sdk.android:twitter-core:$version"
    }
}