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

package co.anitrend.core.migration.model

import android.content.Context
import androidx.core.content.edit
import co.anitrend.core.android.R
import co.anitrend.core.android.settings.Settings

internal object Migrations {
    private val FROM_20290_TO_20300 = object : Migration(20290, 20300) {
        override fun invoke(context: Context, settings: Settings) {
            settings.clearDataOnSwipeRefresh.value = false
        }
    }

    private val FROM_20310_TO_20320 = object : Migration(20310, 20320) {
        override fun invoke(context: Context, settings: Settings) {
            val key = context.getString(R.string.settings_view_mode_preferred)
            settings.edit(commit = true) { remove(key) }
        }
    }

    private val FROM_20320_TO_20330 = object : Migration(20320, 20330) {
        override fun invoke(context: Context, settings: Settings) {
            // remove old preference which is no longer tracked
            settings.edit(commit = true) { remove("_isSortOrderDescending") }
        }
    }
    
    val ALL = listOf(
        FROM_20290_TO_20300,
        FROM_20310_TO_20320,
        FROM_20320_TO_20330
    )
}