/*
 * Copyright (C) 2020 AniTrend
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
package co.anitrend.navigation.drawer.model.state

/**
 * Enumeration of states in which the account picker can be in.
 */
enum class SandwichState {
    /**
     * The account picker is not visible. The navigation drawer is in its default state.
     */
    CLOSED,

    /**
     * the account picker is visible and open.
     */
    OPEN,

    /**
     * The account picker sandwiching animation is running. The account picker is neither open
     * nor closed.
     */
    SETTLING,
}
