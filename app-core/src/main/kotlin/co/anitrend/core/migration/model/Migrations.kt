/*
 * Copyright (C) 2021 AniTrend
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
import co.anitrend.core.android.storage.StorageController

internal object Migrations {
    private val FROM_v2_0_0_28_TO_v2_0_0_30 =
        object : Migration(20290, 20300) {
            override fun invoke(
                context: Context,
                settings: Settings,
            ) {
                settings.clearDataOnSwipeRefresh.value = false
            }
        }

    private val FROM_v2_0_0_31_TO_v2_0_0_32 =
        object : Migration(20310, 20320) {
            override fun invoke(
                context: Context,
                settings: Settings,
            ) {
                val key = context.getString(R.string.settings_view_mode_preferred)
                settings.edit(commit = true) { remove(key) }
            }
        }

    private val FROM_v2_0_0_32_TO_v2_0_0_33 =
        object : Migration(20320, 20330) {
            override fun invoke(
                context: Context,
                settings: Settings,
            ) {
                // remove old preference which is no longer tracked
                settings.edit(commit = true) { remove("_isSortOrderDescending") }
            }
        }

    private val FROM_v2_0_0_33_TO_v2_0_0_39 =
        object : Migration(20330, 2_000_000_039) {
            override fun invoke(
                context: Context,
                settings: Settings,
            ) {
                // remove old preference which is no longer tracked
                settings.edit(commit = true) { remove("_syncInterval") }
            }
        }

    private val FROM_v2_0_0_39_TO_v2_0_0_41 =
        object : Migration(2_000_000_039, 2_000_000_041) {
            override fun invoke(
                context: Context,
                settings: Settings,
            ) {
                // clear image cache along with migration of coil
                StorageController().getImageCache(context = context)
                    .deleteRecursively()
            }
        }

    val ALL =
        listOf(
            FROM_v2_0_0_28_TO_v2_0_0_30,
            FROM_v2_0_0_31_TO_v2_0_0_32,
            FROM_v2_0_0_32_TO_v2_0_0_33,
            FROM_v2_0_0_33_TO_v2_0_0_39,
            FROM_v2_0_0_39_TO_v2_0_0_41,
        )
}
