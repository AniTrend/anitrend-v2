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
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
internal data class CarouselAnimeModel(
    @SerializedName("AiringSoon") val airingSoon: AiringSchedulePageModel.Page?,
    @SerializedName("AllTimePopular") val allTimePopular: MediaPageModel.Page?,
    @SerializedName("TrendingRightNow") val trendingRightNow: MediaPageModel.Page?,
    @SerializedName("PopularThisSeason") val popularThisSeason: MediaPageModel.Page?,
    @SerializedName("RecentlyAdded") val recentlyAdded: MediaPageModel.Page?,
    @SerializedName("AnticipatedNexSeason") val anticipatedNexSeason: MediaPageModel.Page?
)

@Serializable
internal data class CarouselMangaModel(
    @SerializedName("AllTimePopular") val allTimePopular: MediaPageModel.Page?,
    @SerializedName("TrendingRightNow") val trendingRightNow: MediaPageModel.Page?,
    @SerializedName("PopularManhwa") val popularManhwa: MediaPageModel.Page?,
    @SerializedName("RecentlyAdded") val recentlyAdded: MediaPageModel.Page?,
)