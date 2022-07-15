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
import co.anitrend.data.common.CountryCode
import co.anitrend.data.common.model.date.FuzzyDateModel
import co.anitrend.data.link.model.LinkModel
import co.anitrend.data.media.model.contract.IMediaModel
import co.anitrend.data.medialist.model.MediaListModel
import co.anitrend.data.rank.model.RankModel
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

    abstract val rankings: List<RankModel>
    abstract val externalLinks: List<LinkModel>

    abstract val trailer: Trailer?
    abstract val title: Title?
    abstract val coverImage: CoverImage?
    abstract val tags: List<TagModel.Extended>?
    abstract val genres: List<String>?

    abstract val isLocked: Boolean
    abstract val isLicensed: Boolean
    abstract val isRecommendationBlocked: Boolean
    abstract val isFavouriteBlocked: Boolean

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
        @SerialName("rankings") override val rankings: List<RankModel> = emptyList(),
        @SerialName("externalLinks") override val externalLinks: List<LinkModel> = emptyList(),
        @SerialName("trailer") override val trailer: Trailer? = null,
        @SerialName("isLocked") override val isLocked: Boolean = false,
        @SerialName("isLicensed") override val isLicensed: Boolean = false,
        @SerialName("isRecommendationBlocked") override val isRecommendationBlocked: Boolean = false,
        @SerialName("title") override val title: Title? = null,
        @SerialName("coverImage") override val coverImage: CoverImage? = null,
        @SerialName("tags") override val tags: List<TagModel.Extended> = emptyList(),
        @SerialName("genres") override val genres: List<String> = emptyList(),
        @SerialName("idMal") override val idMal: Long? = null,
        @SerialName("bannerImage") override val bannerImage: String? = null,
        @SerialName("type") override val type: MediaType,
        @SerialName("format") override val format: MediaFormat? = null,
        @SerialName("season") override val season: MediaSeason? = null,
        @SerialName("status") override val status: MediaStatus? = null,
        @SerialName("source") override val source: MediaSource? = null,
        @SerialName("meanScore") override val meanScore: Int? = null,
        @SerialName("description") override val description: String? = null,
        @SerialName("averageScore") override val averageScore: Int? = null,
        @SerialName("countryOfOrigin") override val countryOfOrigin: CountryCode? = null,
        @SerialName("trending") override val trending: Int? = null,
        @SerialName("popularity") override val popularity: Int? = null,
        @SerialName("synonyms") override val synonyms: List<String> = emptyList(),
        @SerialName("favourites") override val favourites: Int,
        @SerialName("hashtag") override val hashTag: String? = null,
        @SerialName("siteUrl") override val siteUrl: String,
        @SerialName("startDate") override val startDate: FuzzyDateModel? = null,
        @SerialName("endDate") override val endDate: FuzzyDateModel? = null,
        @SerialName("episodes") override val episodes: Int? = null,
        @SerialName("duration") override val duration: Int? = null,
        @SerialName("chapters") override val chapters: Int? = null,
        @SerialName("volumes") override val volumes: Int? = null,
        @SerialName("isAdult") override val isAdult: Boolean? = null,
        @SerialName("isFavourite") override val isFavourite: Boolean = false,
        @SerialName("isFavouriteBlocked") override val isFavouriteBlocked: Boolean = false,
        @SerialName("updatedAt") override val updatedAt: Long? = null,
        @SerialName("nextAiringEpisode") override val nextAiringEpisode: AiringScheduleModel.Core? = null,
        @SerialName("id") override val id: Long
    ) : MediaModel()

    @Serializable
    internal data class Core(
        @SerialName("mediaListEntry") val mediaListEntry: MediaListModel.Core? = null,
        @SerialName("rankings") override val rankings: List<RankModel> = emptyList(),
        @SerialName("externalLinks") override val externalLinks: List<LinkModel> = emptyList(),
        @SerialName("trailer") override val trailer: Trailer? = null,
        @SerialName("isLocked") override val isLocked: Boolean = false,
        @SerialName("isLicensed") override val isLicensed: Boolean = false,
        @SerialName("isRecommendationBlocked") override val isRecommendationBlocked: Boolean = false,
        @SerialName("title") override val title: Title? = null,
        @SerialName("coverImage") override val coverImage: CoverImage? = null,
        @SerialName("tags") override val tags: List<TagModel.Extended> = emptyList(),
        @SerialName("genres") override val genres: List<String> = emptyList(),
        @SerialName("idMal") override val idMal: Long? = null,
        @SerialName("bannerImage") override val bannerImage: String? = null,
        @SerialName("type") override val type: MediaType,
        @SerialName("format") override val format: MediaFormat? = null,
        @SerialName("season") override val season: MediaSeason? = null,
        @SerialName("status") override val status: MediaStatus? = null,
        @SerialName("source") override val source: MediaSource? = null,
        @SerialName("meanScore") override val meanScore: Int? = null,
        @SerialName("description") override val description: String? = null,
        @SerialName("averageScore") override val averageScore: Int? = null,
        @SerialName("countryOfOrigin") override val countryOfOrigin: CountryCode? = null,
        @SerialName("trending") override val trending: Int? = null,
        @SerialName("popularity") override val popularity: Int? = null,
        @SerialName("synonyms") override val synonyms: List<String> = emptyList(),
        @SerialName("favourites") override val favourites: Int,
        @SerialName("hashtag") override val hashTag: String? = null,
        @SerialName("siteUrl") override val siteUrl: String,
        @SerialName("startDate") override val startDate: FuzzyDateModel? = null,
        @SerialName("endDate") override val endDate: FuzzyDateModel? = null,
        @SerialName("episodes") override val episodes: Int? = null,
        @SerialName("duration") override val duration: Int? = null,
        @SerialName("chapters") override val chapters: Int? = null,
        @SerialName("volumes") override val volumes: Int? = null,
        @SerialName("isAdult") override val isAdult: Boolean? = null,
        @SerialName("isFavourite") override val isFavourite: Boolean = false,
        @SerialName("isFavouriteBlocked") override val isFavouriteBlocked: Boolean = false,
        @SerialName("updatedAt") override val updatedAt: Long? = null,
        @SerialName("nextAiringEpisode") override val nextAiringEpisode: AiringScheduleModel.Core? = null,
        @SerialName("id") override val id: Long
    ) : MediaModel()

    @Serializable
    internal data class Extended(
        @SerialName("mediaListEntry") val mediaListEntry: MediaListModel.Core? = null,
        @SerialName("rankings") override val rankings: List<RankModel> = emptyList(),
        @SerialName("externalLinks") override val externalLinks: List<LinkModel> = emptyList(),
        @SerialName("trailer") override val trailer: Trailer? = null,
        @SerialName("isLocked") override val isLocked: Boolean = false,
        @SerialName("isLicensed") override val isLicensed: Boolean = false,
        @SerialName("isRecommendationBlocked") override val isRecommendationBlocked: Boolean = false,
        @SerialName("title") override val title: Title? = null,
        @SerialName("coverImage") override val coverImage: CoverImage? = null,
        @SerialName("tags") override val tags: List<TagModel.Extended> = emptyList(),
        @SerialName("genres") override val genres: List<String> = emptyList(),
        @SerialName("idMal") override val idMal: Long? = null,
        @SerialName("bannerImage") override val bannerImage: String? = null,
        @SerialName("type") override val type: MediaType,
        @SerialName("format") override val format: MediaFormat? = null,
        @SerialName("season") override val season: MediaSeason? = null,
        @SerialName("status") override val status: MediaStatus? = null,
        @SerialName("source") override val source: MediaSource? = null,
        @SerialName("meanScore") override val meanScore: Int? = null,
        @SerialName("description") override val description: String? = null,
        @SerialName("averageScore") override val averageScore: Int? = null,
        @SerialName("countryOfOrigin") override val countryOfOrigin: CountryCode? = null,
        @SerialName("trending") override val trending: Int? = null,
        @SerialName("popularity") override val popularity: Int? = null,
        @SerialName("synonyms") override val synonyms: List<String> = emptyList(),
        @SerialName("favourites") override val favourites: Int,
        @SerialName("hashtag") override val hashTag: String? = null,
        @SerialName("siteUrl") override val siteUrl: String,
        @SerialName("startDate") override val startDate: FuzzyDateModel? = null,
        @SerialName("endDate") override val endDate: FuzzyDateModel? = null,
        @SerialName("episodes") override val episodes: Int? = null,
        @SerialName("duration") override val duration: Int? = null,
        @SerialName("chapters") override val chapters: Int? = null,
        @SerialName("volumes") override val volumes: Int? = null,
        @SerialName("isAdult") override val isAdult: Boolean? = null,
        @SerialName("isFavourite") override val isFavourite: Boolean = false,
        @SerialName("isFavouriteBlocked") override val isFavouriteBlocked: Boolean = false,
        @SerialName("updatedAt") override val updatedAt: Long? = null,
        @SerialName("nextAiringEpisode") override val nextAiringEpisode: AiringScheduleModel.Core? = null,
        @SerialName("id") override val id: Long
    ) : MediaModel()
}