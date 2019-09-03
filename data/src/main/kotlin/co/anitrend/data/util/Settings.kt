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

package co.anitrend.data.util

import android.content.Context
import co.anitrend.arch.extension.empty
import co.anitrend.arch.extension.preference.SupportPreference

class Settings(context: Context) : SupportPreference(context) {

    var authenticatedUserId: Long = INVALID_USER_ID
        get() = sharedPreferences.getLong(AUTHENTICATED_USER, -1)
        set(value) {
            field = value
            sharedPreferences.edit().putLong(AUTHENTICATED_USER, value).apply()
        }

    // TODO: Implement different types of themes which the application can use
    var appliedTheme: String = String.empty()

    // TODO: Implement preference values for sorting
    var isSortOrderDescending: Boolean = false

    var isAnalyticsEnabled: Boolean = false

    var isCrashlyticsEnabled: Boolean = false

    companion object  {
        const val INVALID_USER_ID: Long = -1
        private const val updateChannel = "updateChannel"
        private const val AUTHENTICATED_USER = "_authenticatedUser"
    }
}