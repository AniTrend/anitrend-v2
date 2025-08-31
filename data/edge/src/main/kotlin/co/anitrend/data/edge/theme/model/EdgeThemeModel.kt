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
package co.anitrend.data.edge.theme.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Theme song (OP/ED) information for an anime media item.
 *
 * Mirrors the original `AnimeThemeType` in the former nested structure.
 * Each entry represents a single theme (opening/ending) and includes a nested meta block
 * describing ordering and variant details.
 *
 * @param audio Optional URL to an audio stream/sample of the theme.
 * @param id Unique identifier of the theme (upstream source id or composite key).
 * @param meta Metadata describing sequencing and classification of the theme.
 * @param name Display/title name of the theme.
 * @param video URL to a video (MV, creditless OP/ED, etc.). May reference streaming host.
 */
@Serializable
internal data class EdgeThemeModel(
    @SerialName("audio") val audio: String? = null,
    @SerialName("id") val id: String,
    @SerialName("meta") val meta: EdgeThemeMetaModel,
    @SerialName("name") val name: String,
    @SerialName("video") val video: String,
) {
    /**
     * Metadata for a theme song.
     *
     * Mirrors the former `AnimeThemeMetaType`.
     *
     * @param number Ordinal number of the theme (e.g. second OP => 2).
     * @param type Theme category (e.g. OP, ED, INSERT).
     * @param version Version/revision of the same numbered theme (e.g., creditless variant).
     */
    @Serializable
    data class EdgeThemeMetaModel(
        @SerialName("number") val number: Int,
        @SerialName("type") val type: String,
        @SerialName("version") val version: Int,
    )
}
