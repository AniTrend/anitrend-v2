/*
 * Copyright (C) 2019  AniTrend
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

package co.anitrend.navigation

import android.content.Context
import co.anitrend.navigation.contract.NavigationComponent
import co.anitrend.navigation.extensions.loadClassOrNull

object NavigationTargets {

    class Main(context: Context) : NavigationComponent(
        context = context,
        packageName = "ui.activity",
        className = "MainScreen"
    ) {
        companion object {
            fun <T> koinInitializer(): Class<out T>? {
                val packagePath = "co.anitrend.initializer"
                val classPath = "$packagePath.KoinInitializer"
                return classPath.loadClassOrNull()
            }
        }
    }

    class OnBoarding(context: Context) : NavigationComponent(
        context = context,
        packageName = "onboarding.ui.activity",
        className = "OnBoardingScreen"
    )

    class Search(context: Context) : NavigationComponent(
        context = context,
        packageName = "search.ui.activity",
        className = "SearchScreen"
    )

    class Settings(context: Context) : NavigationComponent(
        context = context,
        packageName = "settings.ui.activity",
        className = "SettingsScreen"
    )

    class About(context: Context) : NavigationComponent(
        context = context,
        packageName = "about.ui.activity",
        className = "AboutScreen"
    )

    class Discover(context: Context) : NavigationComponent(
        context = context,
        packageName = "media.ui.fragment",
        className = "DiscoverContent"
    )
}