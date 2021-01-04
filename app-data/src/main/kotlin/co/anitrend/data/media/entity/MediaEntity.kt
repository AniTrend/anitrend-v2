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
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import co.anitrend.data.arch.CountryCode
import co.anitrend.data.arch.FuzzyDateInt
import co.anitrend.data.shared.common.Identity
import co.anitrend.domain.media.enums.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(
    tableName = "media",
    primaryKeys = ["id"]
)
internal data class MediaEntity(
    @Embedded(prefix = "cover_") val coverImage: CoverImage,
    @Embedded(prefix = "title_") val title: Title,
    @Embedded(prefix = "trailer_") val trailer: Trailer? = null,
    @ColumnInfo(name = "next_airing_id") val nextAiringId: Long? = null,
    @ColumnInfo(name = "tags") val tags: List<Tag> = emptyList(),
    @ColumnInfo(name = "genres") val genres: List<Genre> = emptyList(),
    @ColumnInfo(name = "links") val links: List<Link> = emptyList(),
    @ColumnInfo(name = "ranks") val ranks: List<Rank> = emptyList(),
    @ColumnInfo(name = "average_score") val averageScore: Int? = null,
    @ColumnInfo(name = "chapters") val chapters: Int? = null,
    @ColumnInfo(name = "country_of_origin") val countryOfOrigin: CountryCode? = null,
    @ColumnInfo(name = "description") val description: String? = null,
    @ColumnInfo(name = "duration") val duration: Int? = null,
    @ColumnInfo(name = "end_date") val endDate: FuzzyDateInt? = null,
    @ColumnInfo(name = "episodes") val episodes: Int? = null,
    @ColumnInfo(name = "favourites") val favourites: Int,
    @ColumnInfo(name = "media_format") val format: MediaFormat? = null,
    @ColumnInfo(name = "hash_tag") val hashTag: String? = null,
    @ColumnInfo(name = "is_adult") val isAdult: Boolean? = null,
    @ColumnInfo(name = "is_favourite") val isFavourite: Boolean,
    @ColumnInfo(name = "is_licensed") val isLicensed: Boolean? = null,
    @ColumnInfo(name = "is_locked") val isLocked: Boolean? = null,
    @ColumnInfo(name = "mean_score") val meanScore: Int? = null,
    @ColumnInfo(name = "popularity") val popularity: Int? = null,
    @ColumnInfo(name = "season") val season: MediaSeason? = null,
    @ColumnInfo(name = "site_url") val siteUrl: String? = null,
    @ColumnInfo(name = "source") val source: MediaSource? = null,
    @ColumnInfo(name = "start_date") val startDate: FuzzyDateInt? = null,
    @ColumnInfo(name = "status") val status: MediaStatus? = null,
    @ColumnInfo(name = "synonyms") val synonyms: List<String> = emptyList(),
    @ColumnInfo(name = "trending") val trending: Int? = null,
    @ColumnInfo(name = "media_type") val type: MediaType,
    @ColumnInfo(name = "updated_at") val updatedAt: Long? = null,
    @ColumnInfo(name = "volumes") val volumes: Int? = null,
    @ColumnInfo(name = "id") override val id: Long
) : Identity {

    internal data class CoverImage(
        @ColumnInfo(name = "color") val color: String? = null,
        @ColumnInfo(name = "extra_large") val extraLarge: String? = null,
        @ColumnInfo(name = "large") val large: String? = null,
        @ColumnInfo(name = "medium") val medium: String? = null,
        @ColumnInfo(name = "banner") val banner: String? = null
    )

    internal data class Title(
        @ColumnInfo(name = "romaji") var romaji: String? = null,
        @ColumnInfo(name = "english") var english: String? = null,
        @ColumnInfo(name = "native") val native: String? = null,
        @ColumnInfo(name = "user_preferred") var userPreferred: String? = null
    )

    internal data class Trailer(
        @ColumnInfo(name = "id") val id: String,
        @ColumnInfo(name = "site") val site: String? = null,
        @ColumnInfo(name = "thumbnail") val thumbnail: String? = null
    )

    @Serializable
    internal data class Tag(
        @SerialName("name") @ColumnInfo(name = "name") val name: String,
        @SerialName("description") @ColumnInfo(name = "description") val description: String? = null,
        @SerialName("category") @ColumnInfo(name = "category") val category: String? = null,
        @SerialName("rank") @ColumnInfo(name = "rank") val rank: Int? = null,
        @SerialName("isGeneralSpoiler") @ColumnInfo(name = "is_general_spoiler") val isGeneralSpoiler: Boolean? = null,
        @SerialName("isMediaSpoiler") @ColumnInfo(name = "is_media_spoiler") val isMediaSpoiler: Boolean? = null,
        @SerialName("isAdult") @ColumnInfo(name = "is_adult") val isAdult: Boolean? = null,
        @SerialName("id") @ColumnInfo(name = "id") override val id: Long
    ) : Identity

    @Serializable
    internal data class Genre(
        @SerialName("name")
        @ColumnInfo(name = "name") val name: String
    )

    @Serializable
    internal data class Link(
        @SerialName("site") @ColumnInfo(name = "site") val site: String,
        @SerialName("url") @ColumnInfo(name = "url") val url: String,
        @SerialName("id") @ColumnInfo(name = "id") override val id: Long
    ) : Identity

    @Serializable
    internal data class Rank(
        @SerialName("allTime") @ColumnInfo(name = "all_time") val allTime: Boolean? = null,
        @SerialName("context") @ColumnInfo(name = "context") val context: String,
        @SerialName("format") @ColumnInfo(name = "format") val format: MediaFormat,
        @SerialName("rank") @ColumnInfo(name = "rank") val rank: Int,
        @SerialName("season") @ColumnInfo(name = "season") val season: MediaSeason? = null,
        @SerialName("type") @ColumnInfo(name = "type") val type: MediaRankType,
        @SerialName("year") @ColumnInfo(name = "year") val year: Int? = null,
        @SerialName("id") @ColumnInfo(name = "id") override val id: Long
    ) : Identity

    internal data class Core(
        @Embedded(prefix = "cover_") val coverImage: CoverImage,
        @Embedded(prefix = "title_") val title: Title,
        @ColumnInfo(name = "next_airing_id") val nextAiringId: Long? = null,
        @ColumnInfo(name = "tags") val tags: List<Tag> = emptyList(),
        @ColumnInfo(name = "genres") val genres: List<Genre> = emptyList(),
        @ColumnInfo(name = "average_score") val averageScore: Int? = null,
        @ColumnInfo(name = "chapters") val chapters: Int? = null,
        @ColumnInfo(name = "country_of_origin") val countryOfOrigin: CountryCode? = null,
        @ColumnInfo(name = "description") val description: String? = null,
        @ColumnInfo(name = "duration") val duration: Int? = null,
        @ColumnInfo(name = "end_date") val endDate: FuzzyDateInt? = null,
        @ColumnInfo(name = "episodes") val episodes: Int? = null,
        @ColumnInfo(name = "favourites") val favourites: Int,
        @ColumnInfo(name = "media_format") val format: MediaFormat? = null,
        @ColumnInfo(name = "hash_tag") val hashTag: String? = null,
        @ColumnInfo(name = "is_adult") val isAdult: Boolean? = null,
        @ColumnInfo(name = "is_favourite") val isFavourite: Boolean,
        @ColumnInfo(name = "is_licensed") val isLicensed: Boolean? = null,
        @ColumnInfo(name = "is_locked") val isLocked: Boolean? = null,
        @ColumnInfo(name = "mean_score") val meanScore: Int? = null,
        @ColumnInfo(name = "popularity") val popularity: Int? = null,
        @ColumnInfo(name = "season") val season: MediaSeason? = null,
        @ColumnInfo(name = "site_url") val siteUrl: String? = null,
        @ColumnInfo(name = "source") val source: MediaSource? = null,
        @ColumnInfo(name = "start_date") val startDate: FuzzyDateInt? = null,
        @ColumnInfo(name = "status") val status: MediaStatus? = null,
        @ColumnInfo(name = "synonyms") val synonyms: List<String> = emptyList(),
        @ColumnInfo(name = "trending") val trending: Int? = null,
        @ColumnInfo(name = "media_type") val type: MediaType,
        @ColumnInfo(name = "updated_at") val updatedAt: Long? = null,
        @ColumnInfo(name = "volumes") val volumes: Int? = null,
        @ColumnInfo(name = "id") override val id: Long
    ) : Identity

    internal data class Extended(
        @Embedded(prefix = "cover_") val coverImage: CoverImage,
        @Embedded(prefix = "title_") val title: Title,
        @ColumnInfo(name = "next_airing_id") val nextAiringId: Long? = null,
        @ColumnInfo(name = "tags") val tags: List<Tag> = emptyList(),
        @ColumnInfo(name = "genres") val genres: List<Genre> = emptyList(),
        @ColumnInfo(name = "links") val links: List<Link> = emptyList(),
        @ColumnInfo(name = "ranks") val ranks: List<Rank> = emptyList(),
        @ColumnInfo(name = "average_score") val averageScore: Int? = null,
        @ColumnInfo(name = "chapters") val chapters: Int? = null,
        @ColumnInfo(name = "country_of_origin") val countryOfOrigin: CountryCode? = null,
        @ColumnInfo(name = "description") val description: String? = null,
        @ColumnInfo(name = "duration") val duration: Int? = null,
        @ColumnInfo(name = "end_date") val endDate: FuzzyDateInt? = null,
        @ColumnInfo(name = "episodes") val episodes: Int? = null,
        @ColumnInfo(name = "favourites") val favourites: Int,
        @ColumnInfo(name = "media_format") val format: MediaFormat? = null,
        @ColumnInfo(name = "hash_tag") val hashTag: String? = null,
        @ColumnInfo(name = "is_adult") val isAdult: Boolean? = null,
        @ColumnInfo(name = "is_favourite") val isFavourite: Boolean,
        @ColumnInfo(name = "is_licensed") val isLicensed: Boolean? = null,
        @ColumnInfo(name = "is_locked") val isLocked: Boolean? = null,
        @ColumnInfo(name = "mean_score") val meanScore: Int? = null,
        @ColumnInfo(name = "popularity") val popularity: Int? = null,
        @ColumnInfo(name = "season") val season: MediaSeason? = null,
        @ColumnInfo(name = "site_url") val siteUrl: String? = null,
        @ColumnInfo(name = "source") val source: MediaSource? = null,
        @ColumnInfo(name = "start_date") val startDate: FuzzyDateInt? = null,
        @ColumnInfo(name = "status") val status: MediaStatus? = null,
        @ColumnInfo(name = "synonyms") val synonyms: List<String> = emptyList(),
        @ColumnInfo(name = "trending") val trending: Int? = null,
        @ColumnInfo(name = "media_type") val type: MediaType,
        @ColumnInfo(name = "updated_at") val updatedAt: Long? = null,
        @ColumnInfo(name = "volumes") val volumes: Int? = null,
        @ColumnInfo(name = "id") override val id: Long
    ) : Identity
}