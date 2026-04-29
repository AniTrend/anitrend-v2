package co.anitrend.buildSrc.plugins.components

import co.anitrend.buildSrc.extensions.androidComponents
import co.anitrend.buildSrc.extensions.hasComposeSupport
import co.anitrend.buildSrc.extensions.isAppModule
import co.anitrend.buildSrc.extensions.isDataGroupModule
import co.anitrend.buildSrc.extensions.isDataModule
import org.gradle.api.Project
import org.gradle.api.plugins.PluginContainer

private fun addAndroidPlugin(project: Project, pluginContainer: PluginContainer) {
    if (project.isAppModule()) pluginContainer.apply("com.android.application")
    else pluginContainer.apply("com.android.library")
}

private fun addAnnotationProcessor(project: Project, pluginContainer: PluginContainer) {
    // TODO: Revisit after https://github.com/AniTrend/anitrend-v2/pull/1076 is merged to default.
    // If support-query-builder is fully KSP-compatible, remove legacy-kapt handling and simplify this branch.
    // KSP for all data modules (Room compiler is KSP-compatible)
    if (project.isDataGroupModule()) {
        pluginContainer.apply("com.google.devtools.ksp")
    }
    // Legacy kapt only for :data (querybuilder processor is not KSP-compatible)
    if (project.isDataModule()) {
        pluginContainer.apply("com.android.legacy-kapt")
    }
}

private fun addKotlinAndroidExtensions(project: Project, pluginContainer: PluginContainer) {
    pluginContainer.apply("kotlin-parcelize")
    pluginContainer.apply("com.diffplug.spotless")
}

internal fun Project.configurePlugins() {
    addAndroidPlugin(project, plugins)
    addKotlinAndroidExtensions(project, plugins)
    addAnnotationProcessor(project, plugins)

    if (project.hasComposeSupport()) {
        plugins.apply("org.jetbrains.kotlin.plugin.compose")
    }
}

internal fun Project.configureAdditionalPlugins() {
    if (isAppModule()) {
        androidComponents().beforeVariants {
            logger.lifecycle("VariantFilter { name: ${it.name}, flavor: ${it.flavorName}, module: $path }")
            if (it.flavorName == "google") {
                logger.lifecycle("Applying additional google plugins on -> module: $path | type: ${it.name}")
                if (file("google-services.json").exists()) {
                    plugins.apply("com.google.gms.google-services")
                    plugins.apply("com.google.firebase.crashlytics")
                } else logger.lifecycle("google-services.json cannot be found and will not be using any of the google plugins")
            }
        }
    }
}
