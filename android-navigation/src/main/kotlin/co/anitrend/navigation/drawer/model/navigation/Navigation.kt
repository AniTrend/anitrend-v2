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

package co.anitrend.navigation.drawer.model.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes

sealed class Navigation {
    abstract val id: Int

    data class Menu(
        override val id: Int,
        @DrawableRes val icon: Int,
        @StringRes val titleRes: Int,
        val isCheckable: Boolean = true,
        var isChecked: Boolean = false
    ) : Navigation()

    data class Divider(
        override val id: Int = DIVIDER
    ) : Navigation()

    data class Group(
        @StringRes val titleRes: Int,
        @IdRes val groupId: Int
    ) : Navigation() {
        override val id: Int = groupId
    }

    companion object {

        internal const val MENU = 1
        internal const val DIVIDER = 2
        internal const val GROUP = 3

        internal fun Navigation?.toNavType(): Int? {
            return when (this) {
                is Menu -> MENU
                is Divider -> DIVIDER
                is Group -> GROUP
                else -> null
            }
        }
    }
}