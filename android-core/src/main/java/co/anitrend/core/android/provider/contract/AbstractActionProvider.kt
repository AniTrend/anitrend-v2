/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.core.android.provider.contract

import android.content.Context
import android.view.MenuItem
import android.view.View
import androidx.core.view.ActionProvider

abstract class AbstractActionProvider(context: Context) : ActionProvider(context) {

    /**
     * Factory for creating the [ActionProvider] view
     *
     * @param forItem Optional menu item to create view for
     */
    protected abstract fun createWidget(forItem: MenuItem? = null): View

    /**
     * Factory method called by the Android framework to create new action views.
     * This method returns a new action view for the given MenuItem.
     *
     * If your ActionProvider implementation overrides the deprecated no-argument overload
     * [onCreateActionView], overriding this method for devices running API 16 or later
     * is recommended but optional. The default implementation calls [onCreateActionView]
     * for compatibility with applications written for older platform versions.
     *
     * @param forItem MenuItem to create the action view for
     * @return the new action view
     */
    override fun onCreateActionView(forItem: MenuItem?) = createWidget(forItem)

    /**
     * Factory method for creating new action views.
     *
     * @return A new action view.
     */
    override fun onCreateActionView() = createWidget()
}