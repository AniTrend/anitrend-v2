/*
 * Copyright (C) 2025 AniTrend
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
package co.anitrend.media.discover.filter.component.compose

import co.anitrend.arch.extension.util.attribute.SeasonType
import co.anitrend.arch.extension.util.date.contract.AbstractSupportDateHelper
import co.anitrend.domain.common.sort.order.SortOrder
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaSeason
import co.anitrend.domain.media.enums.MediaSort
import co.anitrend.domain.media.enums.MediaStatus
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.navigation.MediaDiscoverRouter
import co.anitrend.navigation.model.sorting.Sorting

internal object PreviewData {
    val dateHelper =
        object : AbstractSupportDateHelper() {
            override val currentSeason: SeasonType = SeasonType.FALL

            override fun getCurrentYear(delta: Int) = 2023 + delta
        }
    val mediaDiscoverParam =
        MediaDiscoverRouter.MediaDiscoverParam(
            status = MediaStatus.FINISHED,
            type = MediaType.ANIME,
            season = MediaSeason.SPRING,
            format_in = listOf(MediaFormat.MANGA, MediaFormat.MUSIC),
            sort =
                listOf(
                    Sorting(MediaSort.DURATION, SortOrder.DESC),
                    Sorting(MediaSort.END_DATE, SortOrder.ASC),
                ),
            seasonYear = 1995,
        )
}
