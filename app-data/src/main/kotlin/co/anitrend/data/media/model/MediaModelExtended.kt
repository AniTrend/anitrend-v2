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
import co.anitrend.data.media.model.contract.IMediaModelExtended
import co.anitrend.data.medialist.model.remote.MediaListModelCore
import co.anitrend.data.shared.common.Identity
import co.anitrend.domain.common.CountryCode
import co.anitrend.domain.common.entity.shared.FuzzyDate
import co.anitrend.domain.media.enums.*
import com.google.gson.annotations.SerializedName

/** [Media](https://anilist.github.io/ApiV2-GraphQL-Docs/media.doc.html)
 * Anime or Manga representation
 */
internal data class MediaModelExtended(
    @SerializedName("id") override val id: Long,
    @SerializedName("averageScore") override val averageScore: Int? = null,
    @SerializedName("bannerImage") override val bannerImage: String? = null,
    @SerializedName("chapters") override val chapters: Int? = null,
    @SerializedName("countryOfOrigin") override val countryOfOrigin: CountryCode? = null,
    @SerializedName("coverImage") override val coverImage: CoverImage? = null,
    @SerializedName("description") override val description: String? = null,
    @SerializedName("duration") override val duration: Int? = null,
    @SerializedName("endDate") override val endDate: FuzzyDate? = null,
    @SerializedName("episodes") override val episodes: Int? = null,
    @SerializedName("externalLinks") override val externalLinks: List<ExternalLink>? = null,
    @SerializedName("favourites") override val favourites: Int,
    @SerializedName("format") override val format: MediaFormat? = null,
    @SerializedName("genres") override val genres: List<String>? = null,
    @SerializedName("hashtag") override val hashTag: String? = null,
    @SerializedName("idMal") override val idMal: Long? = null,
    @SerializedName("isAdult") override val isAdult: Boolean? = null,
    @SerializedName("isFavourite") override val isFavourite: Boolean,
    @SerializedName("isLicensed") override val isLicensed: Boolean? = null,
    @SerializedName("isLocked") override val isLocked: Boolean? = null,
    @SerializedName("meanScore") override val meanScore: Int? = null,
    @SerializedName("nextAiringEpisode") override val nextAiringEpisode: AiringScheduleModel? = null,
    @SerializedName("popularity") override val popularity: Int? = null,
    @SerializedName("rankings") override val rankings: List<Rank>? = null,
    @SerializedName("season") override val season: MediaSeason? = null,
    @SerializedName("siteUrl") override val siteUrl: String? = null,
    @SerializedName("source") override val source: MediaSource? = null,
    @SerializedName("startDate") override val startDate: FuzzyDate? = null,
    @SerializedName("status") override val status: MediaStatus? = null,
    @SerializedName("synonyms") override val synonyms: List<String>? = null,
    @SerializedName("tags") override val tags: List<Tag>? = null,
    @SerializedName("title") override val title: Title? = null,
    @SerializedName("trailer") override val trailer: Trailer? = null,
    @SerializedName("trending") override val trending: Int? = null,
    @SerializedName("type") override val type: MediaType,
    @SerializedName("updatedAt") override val updatedAt: Long? = null,
    @SerializedName("volumes") override val volumes: Int? = null,
    @SerializedName("mediaListEntry") override val mediaListEntry: MediaListModelCore? = null
) : IMediaModelExtended {

    /** [MediaCover](https://anilist.github.io/ApiV2-GraphQL-Docs/mediacoverimage.doc.html)
     *
     * @param color Average #hex color of cover image
     * @param extraLarge The cover image url of the media at its largest size.
     * If this size isn't available, [large] will be provided instead.
     * @param large The cover image at its large size
     * @param medium The cover image at medium size
     */
    internal data class CoverImage(
        @SerializedName("color") val color: String? = null,
        @SerializedName("extraLarge") val extraLarge: String? = null,
        @SerializedName("large") val large: String? = null,
        @SerializedName("medium") val medium: String? = null
    )

    /** [MediaExternalLink](https://anilist.github.io/ApiV2-GraphQL-Docs/mediaexternallink.doc.html)
     * An external link to another site related to the media
     */
    internal data class ExternalLink(
        @SerializedName("site") val site: String,
        @SerializedName("url") val url: String,
        @SerializedName("id") override val id: Long
    ) : Identity

    /**
     * Genre as a single element
     */
    internal data class Genre(
        @SerializedName("genre") val genre: String
    )

    /** [MediaRank](https://anilist.github.io/ApiV2-GraphQL-Docs/mediarank.doc.html)
     * The ranking of a media in a particular time span and format compared to other media
     */
    internal data class Rank(
        @SerializedName("allTime") val allTime: Boolean? = null,
        @SerializedName("context") val context: String,
        @SerializedName("format") val format: MediaFormat,
        @SerializedName("rank") val rank: Int,
        @SerializedName("season") val season: MediaSeason? = null,
        @SerializedName("type") val type: MediaRankType,
        @SerializedName("year") val year: Int? = null,
        @SerializedName("id") override val id: Long
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
    internal data class Tag(
        @SerializedName("name") val name: String,
        @SerializedName("description") val description: String? = null,
        @SerializedName("category") val category: String? = null,
        @SerializedName("rank") val rank: Int? = null,
        @SerializedName("isGeneralSpoiler") val isGeneralSpoiler: Boolean? = null,
        @SerializedName("isMediaSpoiler") val isMediaSpoiler: Boolean? = null,
        @SerializedName("isAdult") val isAdult: Boolean? = null,
        @SerializedName("id") override val id: Long
    ) : Identity

    /** [MediaTitle](https://anilist.github.io/ApiV2-GraphQL-Docs/mediatitle.doc.html)
     * The official titles of the media in various languages
     */
    internal data class Title(
        @SerializedName("romaji") val romaji: String? = null,
        @SerializedName("english") val english: String? = null,
        @SerializedName("native") val native: String? = null,
        @SerializedName("userPreferred") val userPreferred: String? = null
    )

    /** [MediaTrailer](https://anilist.github.io/ApiV2-GraphQL-Docs/mediatrailer.doc.html)
     * Media trailer or advertisement
     */
    internal data class Trailer(
        @SerializedName("id") val id: String? = null,
        @SerializedName("site") val site: String? = null,
        @SerializedName("thumbnail") val thumbnail: String? = null
    )
}