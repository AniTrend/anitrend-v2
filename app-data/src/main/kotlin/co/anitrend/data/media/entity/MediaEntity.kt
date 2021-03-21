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
import co.anitrend.data.arch.CountryCode
import co.anitrend.data.arch.FuzzyDateInt
import co.anitrend.data.shared.common.Identity
import co.anitrend.domain.media.enums.*
import co.anitrend.support.query.builder.annotation.EntitySchema

@Entity(
    tableName = "media",
    primaryKeys = ["id"]
)
@EntitySchema
internal data class MediaEntity(
    @Embedded(prefix = "cover_") val coverImage: CoverImage,
    @Embedded(prefix = "title_") val title: Title,
    @Embedded(prefix = "trailer_") val trailer: Trailer? = null,
    @ColumnInfo(name = "next_airing_id") val nextAiringId: Long? = null,
    @ColumnInfo(name = "average_score") val averageScore: Int? = null,
    @ColumnInfo(name = "chapters") val chapters: Int? = null,
    @ColumnInfo(name = "country_of_origin") val countryOfOrigin: CountryCode? = null,
    @ColumnInfo(name = "description") val description: String? = null,
    @ColumnInfo(name = "duration") val duration: Int? = null,
    @ColumnInfo(name = "end_date") val endDate: FuzzyDateInt? = null,
    @ColumnInfo(name = "episodes") val episodes: Int? = null,
    @ColumnInfo(name = "favourites") val favourites: Int = 0,
    @ColumnInfo(name = "media_format") val format: MediaFormat? = null,
    @ColumnInfo(name = "hash_tag") val hashTag: String? = null,
    @ColumnInfo(name = "is_adult") val isAdult: Boolean? = null,
    @ColumnInfo(name = "is_favourite") val isFavourite: Boolean = false,
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
        @ColumnInfo(name = "original") val original: String? = null,
        @ColumnInfo(name = "user_preferred") var userPreferred: String? = null
    )

    internal data class Trailer(
        @ColumnInfo(name = "id") val id: String,
        @ColumnInfo(name = "site") val site: String? = null,
        @ColumnInfo(name = "thumbnail") val thumbnail: String? = null
    )
}