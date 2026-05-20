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

import co.anitrend.buildSrc.extensions.applicationExtension
import co.anitrend.buildSrc.module.Modules
import co.anitrend.buildSrc.plugins.components.PropertiesReader
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.TestPlugin
import com.diffplug.gradle.spotless.SpotlessExtension
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Project
import org.gradle.api.plugins.ExtraPropertiesExtension
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.reporting.ReportingExtension
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.getByType
fun Project.isAppModule() = path == Modules.App.Main.path
fun Project.isDataModule() = path == Modules.Data.Common.path
fun Project.isDomainModule() = path == Modules.Domain.Common.path
fun Project.isCoreModule() = path == Modules.App.Core.path
fun Project.isNavigationModule() = path == Modules.App.Navigation.path
fun Project.isAndroidCoreModule() = path == Modules.Android.Core.path

fun Project.matchesAppModule() = path.startsWith(Modules.APP_MODULE_PATTERN)
fun Project.matchesDataModule() = path.startsWith(Modules.DATA_MODULE_PATTERN)
fun Project.matchesDomainModule() = path.startsWith(Modules.DOMAIN_MODULE_PATTERN)
fun Project.matchesAndroidModule() = path.startsWith(Modules.ANDROID_MODULE_PATTERN)
fun Project.matchesFeatureModule() = path.startsWith(Modules.FEATURE_MODULE_PATTERN)
fun Project.matchesCommonModule() = path.startsWith(Modules.COMMON_MODULE_PATTERN)
fun Project.matchesTaskModule() = path.startsWith(Modules.TASK_MODULE_PATTERN)

fun Project.isDomainGroupModule() = isDomainModule() || matchesDomainModule()
fun Project.isDataGroupModule() = isDataModule() || matchesDataModule()
fun Project.isAppGroupModule() = isAppModule() || matchesAppModule()

/**
 * Module that supports kotlinx-coroutines dependencies
 */
fun Project.hasCoroutineSupport() = path != Modules.App.Navigation.path || !isDomainGroupModule()

/**
 * Module that supports androidx.compose dependencies
 */
fun Project.hasComposeSupport() = isAppModule() || matchesFeatureModule() ||
        matchesAndroidModule() || matchesCommonModule()

/**
 * Module that support [io.insert-koin:koin-androidx-*] dependencies
 */
fun Project.hasKoinAndroidSupport() =
    path != Modules.Data.Common.path || path != Modules.App.Core.path || path != Modules.App.Navigation.path

/**
 * Module that supports the kotlin parcelize plugin.
 */
fun Project.hasParcelizeSupport() =
    path != Modules.Domain.Common.path

/**
 * Data modules use symbol processing for Room and query builder code generation.
 */
fun Project.hasKspSupport() = isDataGroupModule()


internal val Project.props: PropertiesReader
    get() = PropertiesReader(this)

internal val Project.libs: LibrariesForLibs get() =
    extensions.getByType<LibrariesForLibs>()

internal fun Project.applicationExtension() =
    extensions.getByType<ApplicationExtension>()

internal fun Project.libraryExtension() =
    extensions.getByType<LibraryExtension>()

internal fun Project.extraPropertiesExtension() =
    extensions.getByType<ExtraPropertiesExtension>()

internal fun Project.reportingExtension() =
    extensions.getByType<ReportingExtension>()

internal fun Project.sourceSetContainer() =
    extensions.getByType<SourceSetContainer>()

internal fun Project.javaPluginExtension() =
    extensions.getByType<JavaPluginExtension>()

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
        plugin is ApplicationExtension
    }
}

internal fun Project.containsLibraryPlugin(): Boolean {
    return project.plugins.toList().any { plugin ->
        plugin is LibraryPlugin
    }
}

internal fun Project.containsTestPlugin(): Boolean {
    return project.plugins.toList().any { plugin ->
        plugin is TestPlugin
    }
}

internal fun Project.runIfAppModule(body: ApplicationExtension.() -> Unit) {
    if (containsAndroidPlugin())
        body(applicationExtension())
}

internal fun Project.runIfLibraryModule(body: LibraryExtension.() -> Unit) {
    if (containsLibraryPlugin())
        body(libraryExtension())
}
