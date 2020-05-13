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

import co.anitrend.navigation.contract.NavigationComponent

object NavigationTargets {

    object Main : NavigationComponent(
        packageName = "ui.activity",
        className = "MainScreen"
    )

    object OnBoarding : NavigationComponent(
        packageName = "onboarding.ui.activity",
        className = "OnBoardingScreen"
    )

    object Search : NavigationComponent(
        packageName = "search.ui.activity",
        className = "SearchScreen"
    )

    object Settings : NavigationComponent(
        packageName = "settings.ui.activity",
        className = "SettingsScreen"
    )

    object About : NavigationComponent(
        packageName = "about.ui.activity",
        className = "AboutScreen"
    )

    object Discover : NavigationComponent(
        packageName = "media.ui.fragment",
        className = "DiscoverContent"
    )
}