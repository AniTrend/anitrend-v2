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
package co.anitrend.data.edge.trailer.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Trailer / promo video information for a media item.
 *
 * Mirrors the original `SeriesTrailerType` formerly nested inside `EdgeMediaModel`.
 * A media may expose several trailers across different hosting sites.
 *
 * @param id Unique id or key for the trailer on the hosting platform (e.g. YouTube key).
 * @param site Hosting site/platform identifier (e.g. "youtube", "vimeo").
 * @param thumbnail Optional URL to a preview/thumbnail image for the trailer.
 */
@Serializable
internal data class EdgeTrailerModel(
    @SerialName("id") val id: String,
    @SerialName("site") val site: String,
    @SerialName("thumbnail") val thumbnail: String? = null,
)
