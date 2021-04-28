package co.anitrend.support.auth.buildSrc.plugin.components

import co.anitrend.support.auth.buildSrc.plugin.extensions.isLibraryModule
import org.gradle.api.Project

private fun Project.applyModulePlugin() {
    if (isLibraryModule()) {
        plugins.apply("com.android.library")
        plugins.apply("org.jetbrains.dokka")
        plugins.apply("maven-publish")
    }
    else
        plugins.apply("com.android.application")
}

internal fun Project.configurePlugins() {
    applyModulePlugin()
    plugins.apply("com.diffplug.spotless")
    plugins.apply("kotlin-android")
    plugins.apply("kotlin-parcelize")
}