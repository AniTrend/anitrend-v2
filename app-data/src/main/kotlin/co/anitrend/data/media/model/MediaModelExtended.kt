/*
 * Copyright (C) 2019  AniTrend
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

package co.anitrend.data.media.model

import co.anitrend.data.airing.model.AiringScheduleModel
import co.anitrend.data.arch.common.model.date.FuzzyDateModel
import co.anitrend.data.media.model.contract.IMediaModelExtended
import co.anitrend.data.medialist.model.remote.MediaListModelCore
import co.anitrend.data.shared.common.Identity
import co.anitrend.data.arch.CountryCode
import co.anitrend.domain.media.enums.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** [Media](https://anilist.github.io/ApiV2-GraphQL-Docs/media.doc.html)
 * Anime or Manga representation
 */
@Serializable
internal data class MediaModelExtended(
    @SerialName("id") override val id: Long,
    @SerialName("averageScore") override val averageScore: Int? = null,
    @SerialName("bannerImage") override val bannerImage: String? = null,
    @SerialName("chapters") override val chapters: Int? = null,
    @SerialName("countryOfOrigin") override val countryOfOrigin: CountryCode? = null,
    @SerialName("coverImage") override val coverImage: CoverImage? = null,
    @SerialName("description") override val description: String? = null,
    @SerialName("duration") override val duration: Int? = null,
    @SerialName("endDate") override val endDate: FuzzyDateModel? = null,
    @SerialName("episodes") override val episodes: Int? = null,
    @SerialName("externalLinks") override val externalLinks: List<ExternalLink>? = null,
    @SerialName("favourites") override val favourites: Int,
    @SerialName("format") override val format: MediaFormat? = null,
    @SerialName("genres") override val genres: List<String>? = null,
    @SerialName("hashtag") override val hashTag: String? = null,
    @SerialName("idMal") override val idMal: Long? = null,
    @SerialName("isAdult") override val isAdult: Boolean? = null,
    @SerialName("isFavourite") override val isFavourite: Boolean,
    @SerialName("isLicensed") override val isLicensed: Boolean? = null,
    @SerialName("isLocked") override val isLocked: Boolean? = null,
    @SerialName("meanScore") override val meanScore: Int? = null,
    @SerialName("nextAiringEpisode") override val nextAiringEpisode: AiringScheduleModel? = null,
    @SerialName("popularity") override val popularity: Int? = null,
    @SerialName("rankings") override val rankings: List<Rank>? = null,
    @SerialName("season") override val season: MediaSeason? = null,
    @SerialName("siteUrl") override val siteUrl: String? = null,
    @SerialName("source") override val source: MediaSource? = null,
    @SerialName("startDate") override val startDate: FuzzyDateModel? = null,
    @SerialName("status") override val status: MediaStatus? = null,
    @SerialName("synonyms") override val synonyms: List<String>? = null,
    @SerialName("tags") override val tags: List<Tag>? = null,
    @SerialName("title") override val title: Title? = null,
    @SerialName("trailer") override val trailer: Trailer? = null,
    @SerialName("trending") override val trending: Int? = null,
    @SerialName("type") override val type: MediaType,
    @SerialName("updatedAt") override val updatedAt: Long? = null,
    @SerialName("volumes") override val volumes: Int? = null,
    @SerialName("mediaListEntry") override val mediaListEntry: MediaListModelCore? = null
) : IMediaModelExtended {

    /** [MediaCover](https://anilist.github.io/ApiV2-GraphQL-Docs/mediacoverimage.doc.html)
     *
     * @param color Average #hex color of cover image
     * @param extraLarge The cover image url of the media at its largest size.
     * If this size isn't available, [large] will be provided instead.
     * @param large The cover image at its large size
     * @param medium The cover image at medium size
     */
    @Serializable
    internal data class CoverImage(
        @SerialName("color") val color: String? = null,
        @SerialName("extraLarge") val extraLarge: String? = null,
        @SerialName("large") val large: String? = null,
        @SerialName("medium") val medium: String? = null
    )

    /** [MediaExternalLink](https://anilist.github.io/ApiV2-GraphQL-Docs/mediaexternallink.doc.html)
     * An external link to another site related to the media
     */
    @Serializable
    internal data class ExternalLink(
        @SerialName("site") val site: String,
        @SerialName("url") val url: String,
        @SerialName("id") override val id: Long
    ) : Identity

    /**
     * Genre as a single element
     */
    @Serializable
    internal data class Genre(
        @SerialName("genre") val genre: String
    )

    /** [MediaRank](https://anilist.github.io/ApiV2-GraphQL-Docs/mediarank.doc.html)
     * The ranking of a media in a particular time span and format compared to other media
     */
    @Serializable
    internal data class Rank(
        @SerialName("allTime") val allTime: Boolean? = null,
        @SerialName("context") val context: String,
        @SerialName("format") val format: MediaFormat,
        @SerialName("rank") val rank: Int,
        @SerialName("season") val season: MediaSeason? = null,
        @SerialName("type") val type: MediaRankType,
        @SerialName("year") val year: Int? = null,
        @SerialName("id") override val id: Long
    ) : Identity

    /** [MediaTag](https://anilist.github.io/ApiV2-GraphQL-Docs/mediatag.doc.html)
     * A tag that describes a theme or element of the media
     *
     * @param name The name of the tag
     * @param description A general description of the tag
     * @param category The categories of tags this tag belongs to
     * @param rank The relevance ranking of the tag out of the 100 for this media
     * @param isGeneralSpoiler If the tag could be a spoiler for any media
     * @param isMediaSpoiler If the tag is a spoiler for this media
     * @param isAdult If the tag is only for adult 18+ media
     */
    @Serializable
    internal data class Tag(
        @SerialName("name") val name: String,
        @SerialName("description") val description: String? = null,
        @SerialName("category") val category: String? = null,
        @SerialName("rank") val rank: Int? = null,
        @SerialName("isGeneralSpoiler") val isGeneralSpoiler: Boolean? = null,
        @SerialName("isMediaSpoiler") val isMediaSpoiler: Boolean? = null,
        @SerialName("isAdult") val isAdult: Boolean? = null,
        @SerialName("id") override val id: Long
    ) : Identity

    /** [MediaTitle](https://anilist.github.io/ApiV2-GraphQL-Docs/mediatitle.doc.html)
     * The official titles of the media in various languages
     */
    @Serializable
    internal data class Title(
        @SerialName("romaji") val romaji: String? = null,
        @SerialName("english") val english: String? = null,
        @SerialName("native") val native: String? = null,
        @SerialName("userPreferred") val userPreferred: String? = null
    )

    /** [MediaTrailer](https://anilist.github.io/ApiV2-GraphQL-Docs/mediatrailer.doc.html)
     * Media trailer or advertisement
     */
    @Serializable
    internal data class Trailer(
        @SerialName("id") val id: String? = null,
        @SerialName("site") val site: String? = null,
        @SerialName("thumbnail") val thumbnail: String? = null
    )
}