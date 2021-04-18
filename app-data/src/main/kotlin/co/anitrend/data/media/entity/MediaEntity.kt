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
import co.anitrend.data.common.CountryCode
import co.anitrend.data.common.FuzzyDateInt
import co.anitrend.data.core.common.Identity
import co.anitrend.domain.media.enums.*
import co.anitrend.support.query.builder.annotation.EntitySchema

@Entity(
    tableName = "media",
    primaryKeys = ["id"],
    indices = [
        Index(
            value = ["mal_id"],
            unique = true
        )
    ]
)
@EntitySchema
internal data class MediaEntity(
    @Embedded(prefix = "cover_") val coverImage: CoverImage,
    @Embedded(prefix = "title_") val title: Title,
    @Embedded(prefix = "trailer_") val trailer: Trailer?,
    @ColumnInfo(name = "next_airing_id") val nextAiringId: Long?,
    @ColumnInfo(name = "average_score") val averageScore: Int?,
    @ColumnInfo(name = "chapters") val chapters: Int?,
    @ColumnInfo(name = "country_of_origin") val countryOfOrigin: CountryCode?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "duration") val duration: Int?,
    @ColumnInfo(name = "end_date") val endDate: FuzzyDateInt?,
    @ColumnInfo(name = "episodes") val episodes: Int?,
    @ColumnInfo(name = "favourites") val favourites: Int,
    @ColumnInfo(name = "media_format") val format: MediaFormat?,
    @ColumnInfo(name = "hash_tag") val hashTag: String?,
    @ColumnInfo(name = "is_adult") val isAdult: Boolean?,
    @ColumnInfo(name = "is_favourite") val isFavourite: Boolean,
    @ColumnInfo(name = "is_favourite_blocked") val isFavouriteBlocked: Boolean,
    @ColumnInfo(name = "is_licensed") val isLicensed: Boolean?,
    @ColumnInfo(name = "is_recommendation_blocked") val isRecommendationBlocked: Boolean,
    @ColumnInfo(name = "is_locked") val isLocked: Boolean?,
    @ColumnInfo(name = "mean_score") val meanScore: Int?,
    @ColumnInfo(name = "popularity") val popularity: Int?,
    @ColumnInfo(name = "season") val season: MediaSeason?,
    @ColumnInfo(name = "site_url") val siteUrl: String?,
    @ColumnInfo(name = "source") val source: MediaSource?,
    @ColumnInfo(name = "start_date") val startDate: FuzzyDateInt?,
    @ColumnInfo(name = "status") val status: MediaStatus?,
    @ColumnInfo(name = "synonyms") val synonyms: List<String>,
    @ColumnInfo(name = "trending") val trending: Int?,
    @ColumnInfo(name = "media_type") val type: MediaType,
    @ColumnInfo(name = "updated_at") val updatedAt: Long?,
    @ColumnInfo(name = "volumes") val volumes: Int?,
    @ColumnInfo(name = "mal_id") val malId: Long?,
    @ColumnInfo(name = "id") override val id: Long
) : Identity {

    internal data class CoverImage(
        @ColumnInfo(name = "color") val color: String?,
        @ColumnInfo(name = "extra_large") val extraLarge: String?,
        @ColumnInfo(name = "large") val large: String?,
        @ColumnInfo(name = "medium") val medium: String?,
        @ColumnInfo(name = "banner") val banner: String?
    )

    internal data class Title(
        @ColumnInfo(name = "romaji") var romaji: String?,
        @ColumnInfo(name = "english") var english: String?,
        @ColumnInfo(name = "original") val original: String?,
        @ColumnInfo(name = "user_preferred") var userPreferred: String?
    )

    internal data class Trailer(
        @ColumnInfo(name = "id") val id: String,
        @ColumnInfo(name = "site") val site: String?,
        @ColumnInfo(name = "thumbnail") val thumbnail: String?
    )
}