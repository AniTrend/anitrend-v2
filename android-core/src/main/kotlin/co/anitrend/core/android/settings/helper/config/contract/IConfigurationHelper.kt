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

package co.anitrend.core.android.settings.helper.config.contract

import android.content.Context
import androidx.fragment.app.FragmentActivity

/**
 * Configuration helper contract
 *
 * @property themeOverride Theme resource override option
 */
interface IConfigurationHelper {

    var themeOverride: Int?

    /**
     * Creates a new context with configuration
     */
    fun attachContext(context: Context?): Context?

    /**
     * Applies configuration upon the create state of the current activity
     *
     * @param activity
     */
    fun onCreate(activity: FragmentActivity)

    /**
     * Applies configuration upon the resume state of the current activity
     *
     * @param activity
     */
    fun onResume(activity: FragmentActivity)
}