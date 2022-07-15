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

import co.anitrend.data.airing.model.container.AiringScheduleModelContainer
import co.anitrend.data.media.model.container.MediaModelContainer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal sealed class CarouselModel {

    @Serializable
    internal data class Core(
        @SerialName("AllTimePopular") val allTimePopular: MediaModelContainer.Paged.Page? = null,
        @SerialName("TrendingRightNow") val trendingRightNow: MediaModelContainer.Paged.Page? = null,
        @SerialName("RecentlyAdded") val recentlyAdded: MediaModelContainer.Paged.Page?
    ) : CarouselModel()

    @Serializable
    internal data class Anime(
        @SerialName("AiringSoon") val airingSoon: AiringScheduleModelContainer.Paged.Page? = null,
        @SerialName("PopularThisSeason") val popularThisSeason: MediaModelContainer.Paged.Page? = null,
        @SerialName("AnticipatedNexSeason") val anticipatedNexSeason: MediaModelContainer.Paged.Page? = null
    ) : CarouselModel()

    @Serializable
    internal data class Manga(
        @SerialName("PopularManhwa") val popularManhwa: MediaModelContainer.Paged.Page? = null
    ) : CarouselModel()
}