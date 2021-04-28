package co.anitrend.support.auth.buildSrc.plugin.extensions

import co.anitrend.support.auth.buildSrc.module.Modules
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.api.BaseVariantOutput
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.diffplug.gradle.spotless.SpotlessExtension
import org.gradle.api.Project
import org.gradle.api.internal.plugins.DefaultArtifactPublicationSet
import org.gradle.api.plugins.BasePlugin
import org.gradle.api.plugins.ExtraPropertiesExtension
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.reporting.ReportingExtension
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.internal.AndroidExtensionsExtension
import org.jetbrains.kotlin.gradle.testing.internal.KotlinTestsRegistry

fun Project.isLibraryModule() =
    Modules.Support.values().any { module ->
        module.id == name
    }

fun Project.isSampleModule() =
    name == Modules.Sample.App.id

fun Project.isCoreModule() =
    name == Modules.Support.Core.id

fun Project.isFacebookModule() =
    name == Modules.Support.Facebook.id

fun Project.isGoogleModule() =
    name == Modules.Support.Google.id

fun Project.isTwitterModule() =
    name == Modules.Support.Twitter.id

internal fun Project.baseExtension() =
    extensions.getByType<BaseExtension>()

internal fun Project.baseAppExtension() =
    extensions.getByType<BaseAppModuleExtension>()

internal fun Project.extraPropertiesExtension() =
    extensions.getByType<ExtraPropertiesExtension>()

internal fun Project.defaultArtifactPublicationSet() =
    extensions.getByType<DefaultArtifactPublicationSet>()

internal fun Project.reportingExtension() =
    extensions.getByType<ReportingExtension>()

internal fun Project.sourceSetContainer() =
    extensions.getByType<SourceSetContainer>()

internal fun Project.javaPluginExtension() =
    extensions.getByType<JavaPluginExtension>()

internal fun Project.variantOutput() =
    extensions.getByType<BaseVariantOutput>()

internal fun Project.libraryExtension() =
    extensions.getByType<LibraryExtension>()

internal fun Project.kotlinAndroidProjectExtension() =
    extensions.getByType<KotlinAndroidProjectExtension>()

internal fun Project.kotlinTestsRegistry() =
    extensions.getByType<KotlinTestsRegistry>()

internal fun Project.publishingExtension() =
    extensions.getByType<PublishingExtension>()

internal fun Project.spotlessExtension() =
    extensions.getByType<SpotlessExtension>()