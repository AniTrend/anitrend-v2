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

import co.anitrend.navigation.provider.INavigationProvider
import co.anitrend.navigation.router.NavigationRouter
import org.koin.core.inject

object Main : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider
}

object Splash : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider
}

object OnBoarding : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider
}

object Search : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider
}

object Settings : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider
}

object About : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider
}

object Media : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider
}

object Character : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider
}

object Staff : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider
}

object User : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider
}
