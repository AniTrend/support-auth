package co.anitrend.support.auth.buildSrc.plugin.components

import co.anitrend.support.auth.buildSrc.common.Versions
import co.anitrend.support.auth.buildSrc.plugin.extensions.*
import co.anitrend.support.auth.buildSrc.plugin.extensions.baseAppExtension
import co.anitrend.support.auth.buildSrc.plugin.extensions.baseExtension
import co.anitrend.support.auth.buildSrc.plugin.extensions.libraryExtension
import co.anitrend.support.auth.buildSrc.plugin.extensions.spotlessExtension
import com.android.build.gradle.internal.dsl.DefaultConfig
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile
import java.io.File

internal fun Project.configureSpotless(): Unit = spotlessExtension().run {
    kotlin {
        target("**/*.kt")
        targetExclude("$buildDir/**/*.kt", "bin/**/*.kt")
        ktlint(Versions.ktlint).userData(
            mapOf("android" to "true")
        )
        licenseHeaderFile(rootProject.file("spotless/copyright.kt"))
    }
}

@Suppress("UnstableApiUsage")
private fun DefaultConfig.applyAdditionalConfiguration(project: Project) {
    if (project.isSampleModule()) {
        applicationId = "co.anitrend.support.auth.sample"
        project.baseAppExtension().buildFeatures {
            viewBinding = true
        }
    }
    else {
        project.libraryExtension().buildFeatures {
            viewBinding = true
        }
        consumerProguardFiles.add(File("consumer-rules.pro"))
    }

    println("Applying vector drawables configuration for module -> ${project.path}")
    vectorDrawables.useSupportLibrary = true
}

internal fun Project.configureAndroid(): Unit = baseExtension().run {
    compileSdkVersion(Versions.compileSdk)
    defaultConfig {
        minSdkVersion(Versions.minSdk)
        targetSdkVersion(Versions.targetSdk)
        versionCode = Versions.versionCode
        versionName = Versions.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        applyAdditionalConfiguration(project)
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            isTestCoverageEnabled = false
            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),
                "proguard-rules.pro"
            )
        }

        getByName("debug") {
            isMinifyEnabled = false
            isTestCoverageEnabled = true
        }
    }

    packagingOptions {
        exclude("META-INF/NOTICE.txt")
        exclude("META-INF/LICENSE")
        exclude("META-INF/LICENSE.txt")
    }

    sourceSets {
        map { androidSourceSet ->
            androidSourceSet.java.srcDir(
                "src/${androidSourceSet.name}/kotlin"
            )
        }
        if (!project.isSampleModule()) {
            getByName("test") {
                resources.srcDirs(file("src/test/resources"))
            }
        }
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
        unitTests.isReturnDefaultValues = true
    }

    lintOptions {
        isAbortOnError = false
        isIgnoreWarnings = false
        isIgnoreTestSources = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.withType(KotlinCompile::class.java) {
        kotlinOptions {
            allWarningsAsErrors = false
            kotlinOptions {
                allWarningsAsErrors = false
                // Filter out modules that won't be using coroutines
                freeCompilerArgs = if (isSampleModule()) listOf(
                    "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                    "-Xopt-in=kotlinx.coroutines.FlowPreview",
                    "-Xopt-in=kotlinx.coroutines.FlowPreview",
                    "-Xopt-in=kotlin.Experimental"
                ) else listOf("-Xopt-in=kotlin.Experimental")
            }
        }
    }

    tasks.withType(KotlinJvmCompile::class.java) {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}