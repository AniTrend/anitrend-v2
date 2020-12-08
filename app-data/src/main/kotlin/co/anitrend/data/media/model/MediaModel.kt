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

package co.anitrend.data.media.model

import co.anitrend.data.airing.model.AiringScheduleModel
import co.anitrend.data.arch.CountryCode
import co.anitrend.data.arch.common.model.date.FuzzyDateModel
import co.anitrend.data.media.model.contract.IMediaModel
import co.anitrend.data.medialist.model.remote.MediaListModel
import co.anitrend.data.shared.common.Identity
import co.anitrend.data.tag.model.remote.TagModel
import co.anitrend.domain.media.enums.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @property title The official titles of the media in various languages
 * @property coverImage The cover images of the media
 * @property tags The cover images of the media
 * @property genres The cover images of the media
 */
@Serializable
internal sealed class MediaModel : IMediaModel {

    abstract val title: Title?
    abstract val coverImage: CoverImage?
    abstract val tags: List<TagModel.Extended>?
    abstract val genres: List<String>?

    abstract val isLocked: Boolean
    abstract val isLicensed: Boolean
    abstract val isRecommendationBlocked: Boolean

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
     * Genre.graphql as a single element
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

    @Serializable
    internal data class Media(
        @SerialName("isLocked") override val isLocked: Boolean = false,
        @SerialName("isLicensed") override val isLicensed: Boolean = false,
        @SerialName("isRecommendationBlocked") override val isRecommendationBlocked: Boolean = false,
        @SerialName("title") override val title: Title?,
        @SerialName("coverImage") override val coverImage: CoverImage?,
        @SerialName("tags") override val tags: List<TagModel.Extended> = emptyList(),
        @SerialName("genres") override val genres: List<String> = emptyList(),
        @SerialName("idMal") override val idMal: Long?,
        @SerialName("bannerImage") override val bannerImage: String?,
        @SerialName("type") override val type: MediaType,
        @SerialName("format") override val format: MediaFormat?,
        @SerialName("season") override val season: MediaSeason?,
        @SerialName("status") override val status: MediaStatus?,
        @SerialName("source") override val source: MediaSource?,
        @SerialName("meanScore") override val meanScore: Int?,
        @SerialName("description") override val description: String?,
        @SerialName("averageScore") override val averageScore: Int?,
        @SerialName("countryOfOrigin") override val countryOfOrigin: CountryCode?,
        @SerialName("trending") override val trending: Int?,
        @SerialName("popularity") override val popularity: Int?,
        @SerialName("synonyms") override val synonyms: List<String> = emptyList(),
        @SerialName("favourites") override val favourites: Int,
        @SerialName("hashtag") override val hashTag: String?,
        @SerialName("siteUrl") override val siteUrl: String,
        @SerialName("startDate") override val startDate: FuzzyDateModel?,
        @SerialName("endDate") override val endDate: FuzzyDateModel?,
        @SerialName("episodes") override val episodes: Int?,
        @SerialName("duration") override val duration: Int?,
        @SerialName("chapters") override val chapters: Int?,
        @SerialName("volumes") override val volumes: Int?,
        @SerialName("isAdult") override val isAdult: Boolean?,
        @SerialName("isFavourite") override val isFavourite: Boolean = false,
        @SerialName("updatedAt") override val updatedAt: Long?,
        @SerialName("nextAiringEpisode") override val nextAiringEpisode: AiringScheduleModel.Core?,
        @SerialName("id") override val id: Long
    ) : MediaModel()

    @Serializable
    internal data class Core(
        @SerialName("mediaListEntry") val mediaListEntry: MediaListModel.Core?,
        @SerialName("isLocked") override val isLocked: Boolean = false,
        @SerialName("isLicensed") override val isLicensed: Boolean = false,
        @SerialName("isRecommendationBlocked") override val isRecommendationBlocked: Boolean = false,
        @SerialName("title") override val title: Title?,
        @SerialName("coverImage") override val coverImage: CoverImage?,
        @SerialName("tags") override val tags: List<TagModel.Extended> = emptyList(),
        @SerialName("genres") override val genres: List<String> = emptyList(),
        @SerialName("idMal") override val idMal: Long?,
        @SerialName("bannerImage") override val bannerImage: String?,
        @SerialName("type") override val type: MediaType,
        @SerialName("format") override val format: MediaFormat?,
        @SerialName("season") override val season: MediaSeason?,
        @SerialName("status") override val status: MediaStatus?,
        @SerialName("source") override val source: MediaSource?,
        @SerialName("meanScore") override val meanScore: Int?,
        @SerialName("description") override val description: String?,
        @SerialName("averageScore") override val averageScore: Int?,
        @SerialName("countryOfOrigin") override val countryOfOrigin: CountryCode?,
        @SerialName("trending") override val trending: Int?,
        @SerialName("popularity") override val popularity: Int?,
        @SerialName("synonyms") override val synonyms: List<String> = emptyList(),
        @SerialName("favourites") override val favourites: Int,
        @SerialName("hashtag") override val hashTag: String?,
        @SerialName("siteUrl") override val siteUrl: String,
        @SerialName("startDate") override val startDate: FuzzyDateModel?,
        @SerialName("endDate") override val endDate: FuzzyDateModel?,
        @SerialName("episodes") override val episodes: Int?,
        @SerialName("duration") override val duration: Int?,
        @SerialName("chapters") override val chapters: Int?,
        @SerialName("volumes") override val volumes: Int?,
        @SerialName("isAdult") override val isAdult: Boolean?,
        @SerialName("isFavourite") override val isFavourite: Boolean = false,
        @SerialName("updatedAt") override val updatedAt: Long?,
        @SerialName("nextAiringEpisode") override val nextAiringEpisode: AiringScheduleModel.Core?,
        @SerialName("id") override val id: Long
    ) : MediaModel()

    @Serializable
    internal data class Extended(
        @SerialName("mediaListEntry") val mediaListEntry: MediaListModel.Core?,
        @SerialName("trailer") val trailer: Trailer? = null,
        @SerialName("rankings") val rankings: List<Rank> = emptyList(),
        @SerialName("externalLinks") val externalLinks: List<ExternalLink> = emptyList(),
        @SerialName("isLocked") override val isLocked: Boolean = false,
        @SerialName("isLicensed") override val isLicensed: Boolean = false,
        @SerialName("isRecommendationBlocked") override val isRecommendationBlocked: Boolean = false,
        @SerialName("title") override val title: Title?,
        @SerialName("coverImage") override val coverImage: CoverImage?,
        @SerialName("tags") override val tags: List<TagModel.Extended> = emptyList(),
        @SerialName("genres") override val genres: List<String> = emptyList(),
        @SerialName("idMal") override val idMal: Long?,
        @SerialName("bannerImage") override val bannerImage: String?,
        @SerialName("type") override val type: MediaType,
        @SerialName("format") override val format: MediaFormat?,
        @SerialName("season") override val season: MediaSeason?,
        @SerialName("status") override val status: MediaStatus?,
        @SerialName("source") override val source: MediaSource?,
        @SerialName("meanScore") override val meanScore: Int?,
        @SerialName("description") override val description: String?,
        @SerialName("averageScore") override val averageScore: Int?,
        @SerialName("countryOfOrigin") override val countryOfOrigin: CountryCode?,
        @SerialName("trending") override val trending: Int?,
        @SerialName("popularity") override val popularity: Int?,
        @SerialName("synonyms") override val synonyms: List<String> = emptyList(),
        @SerialName("favourites") override val favourites: Int,
        @SerialName("hashtag") override val hashTag: String?,
        @SerialName("siteUrl") override val siteUrl: String,
        @SerialName("startDate") override val startDate: FuzzyDateModel?,
        @SerialName("endDate") override val endDate: FuzzyDateModel?,
        @SerialName("episodes") override val episodes: Int?,
        @SerialName("duration") override val duration: Int?,
        @SerialName("chapters") override val chapters: Int?,
        @SerialName("volumes") override val volumes: Int?,
        @SerialName("isAdult") override val isAdult: Boolean?,
        @SerialName("isFavourite") override val isFavourite: Boolean = false,
        @SerialName("updatedAt") override val updatedAt: Long?,
        @SerialName("nextAiringEpisode") override val nextAiringEpisode: AiringScheduleModel.Core?,
        @SerialName("id") override val id: Long
    ) : MediaModel()
}