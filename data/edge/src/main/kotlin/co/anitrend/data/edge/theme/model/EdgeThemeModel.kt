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

@Serializable
internal data class EdgeThemeModel(
    @SerialName("id") val id: Long? = null,
    @SerialName("sequence") val sequence: Int? = null,
    @SerialName("slug") val slug: String? = null,
    @SerialName("type") val type: String? = null,
    @SerialName("song") val song: SongModel? = null,
    @SerialName("animethemeentries") val entries: List<EntryModel> = emptyList(),
) {
    val isPersistable: Boolean
        get() = !name.isNullOrBlank() || !video.isNullOrBlank()

    val name: String?
        get() = song?.title

    val audio: String?
        get() =
            entries.firstNotNullOfOrNull { entry ->
                entry.videos
                    .firstOrNull()
                    ?.audio
                    ?.link
            }

    val video: String?
        get() = entries.firstNotNullOfOrNull { entry -> entry.videos.firstOrNull()?.link }

    val meta: EdgeThemeMetaModel?
        get() = EdgeThemeMetaModel(number = sequence, type = type, version = entries.firstOrNull()?.version)

    @Serializable
    data class EdgeThemeMetaModel(
        @SerialName("number") val number: Int? = null,
        @SerialName("type") val type: String? = null,
        @SerialName("version") val version: Int? = null,
    )

    @Serializable
    data class SongModel(
        @SerialName("id") val id: Long? = null,
        @SerialName("title") val title: String? = null,
    )

    @Serializable
    data class EntryModel(
        @SerialName("id") val id: Long? = null,
        @SerialName("episodes") val episodes: String? = null,
        @SerialName("notes") val notes: String? = null,
        @SerialName("nsfw") val nsfw: Boolean = false,
        @SerialName("spoiler") val spoiler: Boolean = false,
        @SerialName("version") val version: Int? = null,
        @SerialName("videos") val videos: List<VideoModel> = emptyList(),
    )

    @Serializable
    data class VideoModel(
        @SerialName("id") val id: Long? = null,
        @SerialName("link") val link: String? = null,
        @SerialName("audio") val audio: AudioModel? = null,
        @SerialName("lyrics") val lyrics: Boolean = false,
        @SerialName("nc") val nc: Boolean = false,
        @SerialName("overlap") val overlap: String? = null,
        @SerialName("resolution") val resolution: Int? = null,
        @SerialName("source") val source: String? = null,
        @SerialName("subbed") val subbed: Boolean = false,
        @SerialName("tags") val tags: String? = null,
        @SerialName("uncen") val uncen: Boolean = false,
    )

    @Serializable
    data class AudioModel(
        @SerialName("id") val id: Long? = null,
        @SerialName("link") val link: String? = null,
    )
}
