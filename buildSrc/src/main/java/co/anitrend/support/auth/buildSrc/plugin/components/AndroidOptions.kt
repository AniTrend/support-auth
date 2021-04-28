package co.anitrend.support.auth.buildSrc.plugin.components

import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.get
import co.anitrend.support.auth.buildSrc.common.Versions
import co.anitrend.support.auth.buildSrc.plugin.extensions.*
import co.anitrend.support.auth.buildSrc.plugin.extensions.baseExtension
import co.anitrend.support.auth.buildSrc.plugin.extensions.libraryExtension
import co.anitrend.support.auth.buildSrc.plugin.extensions.publishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.kotlin.dsl.getValue
import java.io.File

private fun Project.configureMavenPublish(sourcesJar: Jar) {
    println("Applying publication configuration on ${project.path}")
    publishingExtension().publications {
        val component = components.findByName("android")

        println("Configuring maven publication options for ${project.path}:maven with component-> ${component?.name}")
        create("maven", MavenPublication::class.java) {
            groupId = "co.anitrend"
            artifactId = "support-auth"
            version = Versions.versionName

            artifact(sourcesJar)
            artifact("${project.buildDir}/outputs/aar/${project.name}-release.aar")
            from(component)

            pom {
                name.set("Support Auth")
                description.set("This is a retrofit converter which uses annotations to inject .graphql query or mutation files into a request body along with any GraphQL variables.")
                url.set("https://github.com/anitrend/support-auth")
                licenses {
                    license {
                        name.set("Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("wax911")
                        name.set("Maxwell Mapako")
                        organizationUrl.set("https://github.com/anitrend")
                    }
                }
            }
        }
    }
}

private fun Project.configureSourceSetJars(): Jar {
    val baseExt = baseExtension()
    val mainSourceSet = baseExt.sourceSets["main"].java.srcDirs

    val sourcesJar by tasks.register("sourcesJar", Jar::class.java) {
        archiveClassifier.set("sources")
        from(mainSourceSet)
    }

    val classesJar by tasks.register("classesJar", Jar::class.java) {
        from("${project.buildDir}/intermediates/classes/release")
    }

    artifacts {
        add("archives", classesJar)
        add("archives", sourcesJar)
    }

    return sourcesJar
}

@Suppress("UnstableApiUsage")
internal fun Project.configureOptions() {
    if (isLibraryModule()) {
        val sourcesJar = configureSourceSetJars()
        configureMavenPublish(sourcesJar)
    }
}