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

package co.anitrend.buildSrc.common

import org.gradle.api.Project

internal const val app = "app"
internal const val core = "app-core"
internal const val data = "app-data"
internal const val domain = "app-domain"
internal const val navigation = "app-navigation"

internal const val androidCore = "android-core"
internal const val onBoarding = "android-onboarding"
internal const val splash = "android-splash"
internal const val auth = "android-auth"

internal const val character = "feature-character"
internal const val staff = "feature-staff"
internal const val studio = "feature-studio"
internal const val media = "feature-media"
internal const val notification = "feature-notification"
internal const val mediaList = "feature-medialist"
internal const val review = "feature-review"
internal const val forum = "feature-forum"
internal const val trending = "feature-trending"
internal const val settings = "feature-settings"
internal const val search = "feature-search"
internal const val profile = "feature-profile"
internal const val account = "feature-account"
internal const val about = "feature-about"
internal const val news = "feature-news"

internal val baseModules = listOf(app, core, data, domain, navigation)

internal val androidModules = listOf(
    androidCore, onBoarding, splash, auth
)

internal val featureModules = listOf(
    character, staff, studio, media, notification, mediaList, review, forum,
    trending, settings, search, profile, account, about, news
)

private const val coreFeatureModulePattern = "android-"
private const val featureModulePattern = "feature-"
private const val commonModulePattern = "common-"
private const val baseModulePattern = "app-"

fun Project.isAppModule() = name == app
fun Project.isDataModule() = name == data
fun Project.isDomainModule() = name == domain
fun Project.isCoreModule() = name == core
fun Project.isNavigationModule() = name == navigation
fun Project.isAndroidCoreModule() = name == androidCore

fun Project.isBaseModule() = name.startsWith(baseModulePattern)
fun Project.isFeatureModule() = name.startsWith(featureModulePattern)
fun Project.isCoreFeatureModule() = name.startsWith(coreFeatureModulePattern)
fun Project.isCommonFeatureModule() = name.startsWith(commonModulePattern)

fun Project.hasCoroutineSupport() = name != navigation || name != domain
fun Project.hasComposeSupport() = isFeatureModule() || isCommonFeatureModule()