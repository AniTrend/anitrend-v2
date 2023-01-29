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

package co.anitrend.core.android.components.action.menu

import android.view.MenuInflater
import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.annotation.MenuRes

interface IFloatingActionMenu {

    /**
     * Find a menu item for the given [id]
     */
    fun findMenuItem(@IdRes id: Int): MenuItem?

    /**
     * Inflate menu
     */
    fun inflate(
        menuInflater: MenuInflater?,
        @MenuRes menuRes: Int,
        clickListener: (item: MenuItem?) -> Boolean
    )

    /**
     * Show action menu
     */
    fun show()

    /**
     * Close action menu
     */
    fun dismiss()
}