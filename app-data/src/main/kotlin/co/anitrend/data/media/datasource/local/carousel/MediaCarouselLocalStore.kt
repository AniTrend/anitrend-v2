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

package co.anitrend.data.media.datasource.local.carousel

import androidx.room.Dao
import androidx.room.Query
import co.anitrend.data.arch.database.dao.ILocalSource
import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.domain.common.CountryCode
import co.anitrend.domain.common.FuzzyDateLike
import co.anitrend.domain.media.enums.MediaSeason
import co.anitrend.domain.media.enums.MediaType
import kotlinx.coroutines.flow.Flow

@Dao
internal abstract class MediaCarouselLocalStore : ILocalSource<MediaEntity> {

    @Query("""
        select count(id) from media
    """)
    abstract override suspend fun count(): Int

    @Query("""
        delete from media
    """)
    abstract override suspend fun clear()

    @Query("""
        select * from media
        where media_type = :mediaType
        and airing_airing_at > :currentTime
        order by airing_time_until_airing desc
    """)
    abstract suspend fun airingSoon(
        mediaType: MediaType = MediaType.ANIME,
        currentTime: Long
    ): List<MediaEntity>

    @Query("""
        select * from media
        where media_type = :mediaType
        and airing_airing_at > :currentTime
        order by airing_time_until_airing asc
    """)
    abstract fun airingSoonFlow(
        mediaType: MediaType = MediaType.ANIME,
        currentTime: Long
    ): Flow<List<MediaEntity>>

    @Query("""
        select * from media
        where media_type = :mediaType
        order by popularity desc
    """)
    abstract suspend fun allTimePopular(
        mediaType: MediaType
    ): List<MediaEntity>

    @Query("""
        select * from media
        where media_type = :mediaType
        order by popularity desc
    """)
    abstract fun allTimePopularFlow(
        mediaType: MediaType
    ): Flow<List<MediaEntity>>

    @Query("""
        select * from media
        where media_type = :mediaType
        order by trending desc
    """)
    abstract suspend fun trendingNow(
        mediaType: MediaType
    ): List<MediaEntity>

    @Query("""
        select * from media
        where media_type = :mediaType
        order by trending desc
    """)
    abstract fun trendingNowFlow(
        mediaType: MediaType
    ): Flow<List<MediaEntity>>

    @Query("""
        select * from media
        where media_type = :mediaType 
        and season = :season 
        and start_date like :seasonYear
        order by popularity desc
    """)
    abstract suspend fun popularThisSeason(
        mediaType: MediaType = MediaType.ANIME,
        season: MediaSeason,
        seasonYear: FuzzyDateLike
    ): List<MediaEntity>

    @Query("""
        select * from media
        where media_type = :mediaType 
        and season = :season 
        and start_date like :seasonYear
        order by popularity desc
    """)
    abstract fun popularThisSeasonFlow(
        mediaType: MediaType = MediaType.ANIME,
        season: MediaSeason,
        seasonYear: FuzzyDateLike
    ): Flow<List<MediaEntity>>

    @Query("""
        select * from media
        where media_type = :mediaType
        order by id desc
    """)
    abstract suspend fun recentlyAdded(
        mediaType: MediaType
    ): List<MediaEntity>

    @Query("""
        select * from media
        where media_type = :mediaType
        order by id desc
    """)
    abstract fun recentlyAddedFlow(
        mediaType: MediaType
    ): Flow<List<MediaEntity>>

    @Query("""
        select * from media
        where media_type = :mediaType
        and season = :season
        and start_date like :seasonYear
        order by popularity desc
    """)
    abstract suspend fun anticipatedNextSeason(
        mediaType: MediaType = MediaType.ANIME,
        season: MediaSeason,
        seasonYear: FuzzyDateLike
    ): List<MediaEntity>

    @Query("""
        select * from media
        where media_type = :mediaType
        and season = :season
        and start_date like :seasonYear
        order by popularity desc
    """)
    abstract fun anticipatedNextSeasonFlow(
        mediaType: MediaType = MediaType.ANIME,
        season: MediaSeason,
        seasonYear: FuzzyDateLike
    ): Flow<List<MediaEntity>>

    @Query("""
        select * from media
        where media_type = :mediaType
        and country_of_origin = :countryCode
        order by popularity desc
    """)
    abstract suspend fun popularManhwa(
        mediaType: MediaType = MediaType.MANGA,
        countryCode: CountryCode = "KR"
    ): List<MediaEntity>

    @Query("""
        select * from media
        where media_type = :mediaType
        and country_of_origin = :countryCode
        order by popularity desc
    """)
    abstract fun popularManhwaFlow(
        mediaType: MediaType = MediaType.MANGA,
        countryCode: CountryCode = "KR"
    ): Flow<List<MediaEntity>>
}