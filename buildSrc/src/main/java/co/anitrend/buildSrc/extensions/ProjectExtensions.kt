/*
 * Copyright (C) 2020  AniTrend
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package co.anitrend.buildSrc.extensions

import co.anitrend.buildSrc.module.Modules
import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.DynamicFeaturePlugin
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.TestPlugin
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.diffplug.gradle.spotless.SpotlessExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.VersionConstraint
import org.gradle.api.internal.plugins.DefaultArtifactPublicationSet
import org.gradle.api.plugins.ExtraPropertiesExtension
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.provider.Provider
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.reporting.ReportingExtension
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.getByType
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.getByName
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.testing.internal.KotlinTestsRegistry


fun Project.isAppModule() = name == Modules.App.Main.id
fun Project.isDataModule() = name == Modules.App.Data.id
fun Project.isDomainModule() = name == Modules.App.Domain.id
fun Project.isCoreModule() = name == Modules.App.Core.id
fun Project.isNavigationModule() = name == Modules.App.Navigation.id
fun Project.isAndroidCoreModule() = name == Modules.Android.Core.id

fun Project.matchesAppModule() = name.startsWith(Modules.appModulePattern)
fun Project.matchesDataModule() = name.startsWith(Modules.dataModulePattern)
fun Project.matchesAndroidModule() = name.startsWith(Modules.androidModulePattern)
fun Project.matchesFeatureModule() = name.startsWith(Modules.featureModulePattern)
fun Project.matchesCommonModule() = name.startsWith(Modules.commonModulePattern)
fun Project.matchesTaskModule() = name.startsWith(Modules.taskModulePattern)

/**
 * Module that supports kotlinx-coroutines dependencies
 */
fun Project.hasCoroutineSupport() = name != Modules.App.Navigation.id || name != Modules.App.Domain.id

/**
 * Module that supports androidx.compose dependencies
 */
fun Project.hasComposeSupport() = isAppModule() || matchesFeatureModule() ||
        matchesAndroidModule() || matchesCommonModule()

/**
 * Module that support [io.insert-koin:koin-androidx-*] dependencies
 */
fun Project.hasKoinAndroidSupport() = 
    name != Modules.App.Data.id || name != Modules.App.Core.id || name != Modules.App.Navigation.id

/**
 * Module that supports the kotlin annotation processor plugin
 */
fun Project.hasKaptSupport() =
    name != Modules.App.Main.id || name != Modules.App.Data.id || name != Modules.App.Core.id

/**
 * Module that supports the kotlin-android-extensions annotation processor plugin
 */
fun Project.hasKotlinAndroidExtensionSupport() =
    name != Modules.App.Domain.id

internal val Project.libs: LibrariesForLibs get() =
    extensions.getByType<LibrariesForLibs>()

internal fun Project.baseExtension() =
    extensions.getByType<BaseExtension>()

internal fun Project.baseAppExtension() =
    extensions.getByType<BaseAppModuleExtension>()

internal fun Project.libraryExtension() =
    extensions.getByType<LibraryExtension>()

internal fun Project.dynamicFeatureExtension() =
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

internal fun Project.kotlinAndroidProjectExtension() =
    extensions.getByType<KotlinAndroidProjectExtension>()

internal fun Project.kotlinTestsRegistry() =
    extensions.getByType<KotlinTestsRegistry>()

internal fun Project.publishingExtension() =
    extensions.getByType<PublishingExtension>()

internal fun Project.spotlessExtension() =
    extensions.getByType<SpotlessExtension>()

internal fun Project.androidComponents() =
    extensions.getByType<ApplicationAndroidComponentsExtension>()

internal fun Project.libraryAndroidComponents() =
    extensions.getByType<LibraryAndroidComponentsExtension>()

internal fun Project.containsAndroidPlugin(): Boolean {
    return project.plugins.toList().any { plugin ->
        plugin is BaseAppModuleExtension
    }
}

internal fun Project.containsLibraryPlugin(): Boolean {
    return project.plugins.toList().any { plugin ->
        plugin is LibraryPlugin
    }
}

internal fun Project.containsDynamicFeaturePlugin(): Boolean {
    return project.plugins.toList().any { plugin ->
        plugin is DynamicFeaturePlugin
    }
}

internal fun Project.containsTestPlugin(): Boolean {
    return project.plugins.toList().any { plugin ->
        plugin is TestPlugin
    }
}

internal fun Project.runIfAppModule(body: BaseAppModuleExtension.() -> Unit) {
    if (containsAndroidPlugin())
        body(baseAppExtension())
}

internal fun Project.runIfLibraryModule(body: LibraryExtension.() -> Unit) {
    if (containsLibraryPlugin())
        body(libraryExtension())
}