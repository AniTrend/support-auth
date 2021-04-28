package co.anitrend.support.auth.buildSrc.plugin.components

import co.anitrend.support.auth.buildSrc.plugin.extensions.implementation
import co.anitrend.support.auth.buildSrc.plugin.strategy.DependencyStrategy
import co.anitrend.support.auth.buildSrc.Libraries
import org.gradle.api.Project

private fun Project.mainDependencies() {
    dependencies.implementation(Libraries.AndroidX.Core.coreKtx)
    dependencies.implementation(Libraries.Google.Material.material)
}

internal fun Project.configureDependencies() {
    val dependencyStrategy = DependencyStrategy(project)
    dependencies.implementation(
        fileTree("libs") {
            include("*.jar")
        }
    )
    dependencyStrategy.applyDependenciesOn(dependencies)
    mainDependencies()
}