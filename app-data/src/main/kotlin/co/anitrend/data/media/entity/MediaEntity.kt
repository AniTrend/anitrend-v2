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

package co.anitrend.data.media.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import co.anitrend.data.airing.model.remote.AiringSchedule
import co.anitrend.data.media.model.contract.IMediaTag
import co.anitrend.data.media.model.remote.*
import co.anitrend.data.media.model.remote.MediaExternalLink
import co.anitrend.data.media.model.remote.MediaRank
import co.anitrend.data.media.model.remote.MediaTitle
import co.anitrend.data.media.model.remote.MediaTrailer
import co.anitrend.data.shared.common.Identity
import co.anitrend.domain.common.CountryCode
import co.anitrend.domain.common.FuzzyDateInt
import co.anitrend.domain.common.entity.shared.FuzzyDate
import co.anitrend.domain.media.enums.*

@Entity(
    tableName = "media_entity"
)
internal data class MediaEntity(
    @PrimaryKey @ColumnInfo(name = "id") override val id: Long,
    @ColumnInfo(name = "average_score") val averageScore: Int? = null,
    @ColumnInfo(name = "banner_image") val bannerImage: String? = null,
    @ColumnInfo(name = "chapters") val chapters: Int? = null,
    @ColumnInfo(name = "country_of_origin") val countryOfOrigin: CountryCode? = null,
    @ColumnInfo(name = "cover_image") val coverImage: MediaCoverImage? = null,
    @ColumnInfo(name = "description") val description: String? = null,
    @ColumnInfo(name = "duration") val duration: Int? = null,
    @ColumnInfo(name = "end_date") val endDate: FuzzyDateInt? = null,
    @ColumnInfo(name = "episodes") val episodes: Int? = null,
    @ColumnInfo(name = "external_links") val externalLinks: List<MediaExternalLink> = emptyList(),
    @ColumnInfo(name = "favourites") val favourites: Int,
    @ColumnInfo(name = "format") val format: MediaFormat? = null,
    @ColumnInfo(name = "genres") val genres: List<String>? = null,
    @ColumnInfo(name = "hash_tag") val hashTag: String? = null,
    @ColumnInfo(name = "id_mal") val idMal: Long? = null,
    @ColumnInfo(name = "is_adult") val isAdult: Boolean? = null,
    @ColumnInfo(name = "is_favourite") val isFavourite: Boolean,
    @ColumnInfo(name = "is_licensed") val isLicensed: Boolean? = null,
    @ColumnInfo(name = "is_locked") val isLocked: Boolean? = null,
    @ColumnInfo(name = "mean_score") val meanScore: Int? = null,
    @ColumnInfo(name = "next_airing_episode") val nextAiringEpisode: AiringSchedule? = null,
    @ColumnInfo(name = "popularity") val popularity: Int? = null,
    @ColumnInfo(name = "rankings") val rankings: List<MediaRank>? = emptyList(),
    @ColumnInfo(name = "season") val season: MediaSeason? = null,
    @ColumnInfo(name = "site_url") val siteUrl: String? = null,
    @ColumnInfo(name = "source") val source: MediaSource? = null,
    @ColumnInfo(name = "start_date") val startDate: FuzzyDateInt? = null,
    @ColumnInfo(name = "status") val status: MediaStatus? = null,
    @ColumnInfo(name = "synonyms") val synonyms: List<String>? = null,
    @ColumnInfo(name = "tags") val tags: List<IMediaTag>? = null,
    @ColumnInfo(name = "title") val title: MediaTitle? = null,
    @ColumnInfo(name = "trailer") val trailer: MediaTrailer? = null,
    @ColumnInfo(name = "trending") val trending: Int? = null,
    @ColumnInfo(name = "type") val type: MediaType? = null,
    @ColumnInfo(name = "updated_at") val updatedAt: Long? = null,
    @ColumnInfo(name = "volumes") val volumes: Int? = null
) : Identity