/*
 * Copyright (C) 2024 AniTrend
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
package co.anitrend.data.edge.config.model.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class EdgeConfigModel(
    @SerialName("settings") val settings: Settings,
    @SerialName("image") val defaultImage: DefaultImage,
    @SerialName("id") val id: Long,
) {
    @Serializable
    data class Settings(
        @SerialName("analyticsEnabled") val analyticsEnabled: Boolean,
    )

    @Serializable
    data class DefaultImage(
        @SerialName("banner") val banner: String,
        @SerialName("poster") val poster: String = "",
        @SerialName("loading") val loading: String = "",
        @SerialName("error") val error: String = "",
        @SerialName("info") val info: String = "",
        @SerialName("default") val default: String = "",
    )
}
