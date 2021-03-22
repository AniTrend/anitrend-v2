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

package co.anitrend.data.jikan.model.anime

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
internal sealed class JikanMediaModel {

    abstract val malId: Long
    abstract val url: String?
    abstract val imageUrl: String?
    abstract val title: String?
    abstract val titleEnglish: String?
    abstract val titleJapanese: String?
    abstract val titleSynonyms: List<String>?
    abstract val type: String?
    abstract val releasing: Boolean?
    abstract val synopsis: String?
    abstract val background: String?
    abstract val moreInfo: String?

    @Serializable
    data class Attribute(
        @SerialName("mal_id") val malId: Long,
        @SerialName("type") val type: String? = null,
        @SerialName("name") val name: String? = null,
        @SerialName("url") val url: String? = null
    )

    @Serializable
    data class MoreInfo(
        @SerialName("moreinfo") val moreInfo: String?
    )

    @Serializable
    data class Anime(
        @SerialName("source") val source: String?,
        @SerialName("rating") val rating: String?,
        @SerialName("episodes") val episodes: Int?,
        @SerialName("duration") val duration: String?,
        @SerialName("premiered") val premiered: String?,
        @SerialName("broadcast") val broadcast: String?,
        @SerialName("trailer_url") val trailerUrl: String?,
        @SerialName("producers") val producers: List<Attribute>?,
        @SerialName("licensors") val licensors: List<Attribute>?,
        @SerialName("studios") val studios: List<Attribute>?,
        @SerialName("opening_themes") val openingThemes: List<String>?,
        @SerialName("ending_themes") val endingThemes: List<String>?,
        @SerialName("mal_id") override val malId: Long,
        @SerialName("url") override val url: String?,
        @SerialName("image_url") override val imageUrl: String?,
        @SerialName("title") override val title: String?,
        @SerialName("title_english") override val titleEnglish: String?,
        @SerialName("title_japanese") override val titleJapanese: String?,
        @SerialName("title_synonyms") override val titleSynonyms: List<String>?,
        @SerialName("type") override val type: String?,
        @SerialName("airing") override val releasing: Boolean?,
        @SerialName("synopsis") override val synopsis: String?,
        @SerialName("background") override val background: String?,
        @Transient override val moreInfo: String? = null
    ) : JikanMediaModel()

    @Serializable
    data class Manga(
        @SerialName("volumes") val volumes: Int?,
        @SerialName("chapters") val chapters: Int?,
        @SerialName("authors") val authors: List<Attribute>?,
        @SerialName("mal_id") override val malId: Long,
        @SerialName("url") override val url: String?,
        @SerialName("image_url") override val imageUrl: String?,
        @SerialName("title") override val title: String?,
        @SerialName("title_english") override val titleEnglish: String?,
        @SerialName("title_japanese") override val titleJapanese: String?,
        @SerialName("title_synonyms") override val titleSynonyms: List<String>?,
        @SerialName("type") override val type: String?,
        @SerialName("publishing") override val releasing: Boolean?,
        @SerialName("synopsis") override val synopsis: String?,
        @SerialName("background") override val background: String?,
        @Transient override val moreInfo: String? = null
    ) : JikanMediaModel()
}