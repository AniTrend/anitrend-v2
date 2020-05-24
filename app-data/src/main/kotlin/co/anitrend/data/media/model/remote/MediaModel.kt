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

package co.anitrend.data.media.model.remote

import co.anitrend.data.airing.model.remote.AiringSchedule
import co.anitrend.data.media.model.contract.IMedia
import co.anitrend.domain.common.CountryCode
import co.anitrend.domain.common.model.FuzzyDate
import co.anitrend.domain.common.model.MediaCover
import co.anitrend.domain.media.enums.*
import com.google.gson.annotations.SerializedName

/** [Media](https://anilist.github.io/ApiV2-GraphQL-Docs/media.doc.html)
 * Anime or Manga representation
 */
internal data class MediaModel(
    @SerializedName("id") override val id: Long,
    @SerializedName("averageScore") override val averageScore: Int?,
    @SerializedName("bannerImage") override val bannerImage: String?,
    @SerializedName("chapters") override val chapters: Int?,
    @SerializedName("countryOfOrigin") override val countryOfOrigin: CountryCode?,
    @SerializedName("coverImage") override val coverImage: MediaCover?,
    @SerializedName("description") override val description: String?,
    @SerializedName("duration") override val duration: Int?,
    @SerializedName("endDate") override val endDate: FuzzyDate?,
    @SerializedName("episodes") override val episodes: Int?,
    @SerializedName("externalLinks") override val externalLinks: List<MediaExternalLink>?,
    @SerializedName("favourites") override val favourites: Int,
    @SerializedName("format") override val format: MediaFormat?,
    @SerializedName("genres") override val genres: List<String>?,
    @SerializedName("hashtag") override val hashtag: String?,
    @SerializedName("idMal") override val idMal: Long?,
    @SerializedName("isAdult") override val isAdult: Boolean?,
    @SerializedName("isFavourite") override val isFavourite: Boolean,
    @SerializedName("isLicensed") override val isLicensed: Boolean?,
    @SerializedName("isLocked") override val isLocked: Boolean?,
    @SerializedName("meanScore") override val meanScore: Int?,
    @SerializedName("nextAiringEpisode") override val nextAiringEpisode: AiringSchedule?,
    @SerializedName("popularity") override val popularity: Int?,
    @SerializedName("rankings") override val rankings: List<MediaRank>?,
    @SerializedName("season") override val season: MediaSeason?,
    @SerializedName("siteUrl") override val siteUrl: String?,
    @SerializedName("source") override val source: MediaSource?,
    @SerializedName("startDate") override val startDate: FuzzyDate?,
    @SerializedName("status") override val status: MediaStatus?,
    @SerializedName("synonyms") override val synonyms: List<String>?,
    @SerializedName("tags") override val tags: List<MediaTag>?,
    @SerializedName("title") override val title: MediaTitle?,
    @SerializedName("trailer") override val trailer: MediaTrailer?,
    @SerializedName("trending") override val trending: Int?,
    @SerializedName("type") override val type: MediaType?,
    @SerializedName("updatedAt") override val updatedAt: Long?,
    @SerializedName("volumes") override val volumes: Int?
) : IMedia