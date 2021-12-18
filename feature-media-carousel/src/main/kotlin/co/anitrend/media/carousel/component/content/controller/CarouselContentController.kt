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

package co.anitrend.media.carousel.component.content.controller

import android.content.res.Resources
import co.anitrend.arch.extension.lifecycle.SupportLifecycle
import co.anitrend.arch.extension.util.attribute.SeasonType
import co.anitrend.arch.extension.util.date.contract.AbstractSupportDateHelper
import co.anitrend.domain.media.enums.MediaSeason
import co.anitrend.media.carousel.R
import org.threeten.bp.Instant

class CarouselContentController(
    dateHelper: AbstractSupportDateHelper
): SupportLifecycle {

    val year: Int = dateHelper.getCurrentYear()

    val season = when (dateHelper.currentSeason) {
        SeasonType.WINTER -> MediaSeason.WINTER
        SeasonType.SPRING -> MediaSeason.SPRING
        SeasonType.SUMMER -> MediaSeason.SUMMER
        SeasonType.FALL -> MediaSeason.FALL
    }

    val nextSeasonYear = when (season) {
        MediaSeason.FALL -> year + 1
        MediaSeason.SPRING -> year
        MediaSeason.SUMMER -> year
        MediaSeason.WINTER -> year
    }

    val nextSeason = when (dateHelper.currentSeason) {
        SeasonType.WINTER -> MediaSeason.SPRING
        SeasonType.SPRING -> MediaSeason.SUMMER
        SeasonType.SUMMER -> MediaSeason.FALL
        SeasonType.FALL -> MediaSeason.WINTER
    }

    fun currentTimeAsEpoch() = Instant.now().epochSecond

    /**
     * Page size depending on the configuration of the form factor in addition to [multiplier]
     */
    fun pageSize(resources: Resources, multiplier: Int) =
        resources.getInteger(R.integer.grid_list_x3)
            .times(multiplier)
}