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
package co.anitrend.data.edge.image.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Backdrop/poster/logo image information.
 *
 * @param url URL to the backdrop image
 * @param height Height of the backdrop image in pixels
 * @param locale Locale of the backdrop image (e.g., en, ja)
 * @param width Width of the backdrop image in pixels
 * @param type Type of image backdrop, logo, poster
 */
@Serializable
data class EdgeImageModel(
    @SerialName("url") val url: String,
    @SerialName("height") val height: Int,
    @SerialName("width") val width: Int,
    @SerialName("locale") val locale: String?,
    @SerialName("type") val type: ImageType,
) {
    @Serializable
    enum class ImageType(
        val alias: String,
    ) {
        @SerialName("backdrop")
        BACKDROP("backdrop"),

        @SerialName("logo")
        LOGO("logo"),

        @SerialName("poster")
        POSTER("poster"),
    }
}
