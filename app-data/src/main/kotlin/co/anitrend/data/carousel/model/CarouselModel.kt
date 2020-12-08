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

package co.anitrend.data.carousel.model

import co.anitrend.data.airing.model.page.AiringSchedulePageModel
import co.anitrend.data.media.model.page.MediaPageModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal sealed class CarouselModel {

    @Serializable
    internal data class Core(
        @SerialName("AllTimePopular") val allTimePopular: MediaPageModel.Page?,
        @SerialName("TrendingRightNow") val trendingRightNow: MediaPageModel.Page?,
        @SerialName("RecentlyAdded") val recentlyAdded: MediaPageModel.Page?
    ) : CarouselModel()

    @Serializable
    internal data class Anime(
        @SerialName("AiringSoon") val airingSoon: AiringSchedulePageModel.Page?,
        @SerialName("PopularThisSeason") val popularThisSeason: MediaPageModel.Page?,
        @SerialName("AnticipatedNexSeason") val anticipatedNexSeason: MediaPageModel.Page?
    ) : CarouselModel()

    @Serializable
    internal data class Manga(
        @SerialName("PopularManhwa") val popularManhwa: MediaPageModel.Page?
    ) : CarouselModel()
}