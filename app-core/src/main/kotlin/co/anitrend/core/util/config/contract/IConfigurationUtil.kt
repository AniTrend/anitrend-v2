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

package co.anitrend.core.util.config.contract

import co.anitrend.core.ui.activity.AnitrendActivity

interface IConfigurationUtil {

    val moduleTag: String

    /**
     * Applies configuration upon the create state of the current activity
     *
     * @param activity
     */
    fun onCreate(activity: AnitrendActivity<*, *>)

    /**
     * Applies configuration upon the resume state of the current activity
     *
     * @param activity
     */
    fun onResume(activity: AnitrendActivity<*, *>)
}