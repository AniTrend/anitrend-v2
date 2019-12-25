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

import co.anitrend.navigation.contract.INavigationRouter
import co.anitrend.navigation.contract.INavigationTarget
import co.anitrend.navigation.extensions.forIntent

object NavigationTargets {

    object Main : INavigationRouter, INavigationTarget {
        override val packageName = "ui.activity"
        override val className = "MainScreen"

        override val navRouterIntent = forIntent()
    }

    object OnBoarding : INavigationRouter, INavigationTarget {
        override val packageName = "onboarding.ui.activity"
        override val className = "OnBoardingScreen"

        override val navRouterIntent = forIntent()
    }

    object Search : INavigationRouter, INavigationTarget {
        override val packageName = "search.ui.activity"
        override val className = "SearchScreen"

        override val navRouterIntent = forIntent()
    }

    object Settings : INavigationRouter, INavigationTarget {
        override val packageName = "settings.ui.activity"
        override val className = "SettingsScreen"

        override val navRouterIntent = forIntent()
    }
}