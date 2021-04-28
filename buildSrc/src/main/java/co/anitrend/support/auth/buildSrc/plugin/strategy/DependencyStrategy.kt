package co.anitrend.support.auth.buildSrc.plugin.strategy

import co.anitrend.support.auth.buildSrc.Libraries
import co.anitrend.support.auth.buildSrc.plugin.extensions.androidTest
import co.anitrend.support.auth.buildSrc.plugin.extensions.implementation
import co.anitrend.support.auth.buildSrc.plugin.extensions.test
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler

internal class DependencyStrategy(
    private val project: Project
) {

    private fun DependencyHandler.applyLoggingDependencies() {
        implementation(Libraries.timber)
    }

    private fun DependencyHandler.applyDefaultDependencies() {
        implementation(Libraries.JetBrains.Kotlin.stdlib)

        // Testing libraries
        test(Libraries.junit)
        test(Libraries.mockk)
    }

    private fun DependencyHandler.applyTestDependencies() {
        androidTest(Libraries.AndroidX.Test.coreKtx)
        androidTest(Libraries.AndroidX.Test.rules)
        androidTest(Libraries.AndroidX.Test.runner)
        androidTest(Libraries.AndroidX.Test.Extension.junitKtx)
    }

    private fun DependencyHandler.applyLifeCycleDependencies() {
        implementation(Libraries.AndroidX.Lifecycle.liveDataCoreKtx)
        implementation(Libraries.AndroidX.Lifecycle.runTimeKtx)
        implementation(Libraries.AndroidX.Lifecycle.liveDataKtx)
        implementation(Libraries.AndroidX.Lifecycle.extensions)
    }

    private fun DependencyHandler.applyCoroutinesDependencies() {
        implementation(Libraries.JetBrains.KotlinX.Coroutines.android)
        implementation(Libraries.JetBrains.KotlinX.Coroutines.core)
        test(Libraries.JetBrains.KotlinX.Coroutines.test)
        androidTest(Libraries.CashApp.Turbine.turbine)
    }

    fun applyDependenciesOn(handler: DependencyHandler) {
        handler.applyDefaultDependencies()
        handler.applyLifeCycleDependencies()
        handler.applyLoggingDependencies()
        handler.applyTestDependencies()
        handler.applyCoroutinesDependencies()
    }
}