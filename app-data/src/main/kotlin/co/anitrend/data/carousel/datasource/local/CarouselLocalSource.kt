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

package co.anitrend.data.carousel.datasource.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import co.anitrend.data.android.source.local.AbstractLocalSource
import co.anitrend.data.common.CountryCode
import co.anitrend.data.common.FuzzyDateLike
import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.data.media.entity.view.MediaEntityView
import co.anitrend.domain.media.enums.MediaSeason
import co.anitrend.domain.media.enums.MediaType
import kotlinx.coroutines.flow.Flow

@Dao
internal abstract class CarouselLocalSource : AbstractLocalSource<MediaEntity>() {

    @Query("""
        select count(id) from media
    """)
    abstract override suspend fun count(): Int

    @Query("""
        delete from media
    """)
    abstract override suspend fun clear()

    @Query("""
        select m.* from media m
        join airing_schedule a on a.id = m.next_airing_id
        where m.media_type = :mediaType
        and a.airing_at > :currentTime
        order by a.time_until_airing asc
        limit :pageSize
    """)
    @Transaction
    abstract fun airingSoonFlow(
        mediaType: MediaType = MediaType.ANIME,
        pageSize: Int,
        currentTime: Long
    ): Flow<List<MediaEntityView.Core>>

    @Query("""
        select * from media
        where media_type = :mediaType
        order by popularity desc
        limit :pageSize
    """)
    @Transaction
    abstract fun allTimePopularFlow(
        mediaType: MediaType,
        pageSize: Int
    ): Flow<List<MediaEntityView.Core>>

    @Query("""
        select * from media
        where media_type = :mediaType
        order by trending desc
        limit :pageSize
    """)
    @Transaction
    abstract fun trendingNowFlow(
        mediaType: MediaType,
        pageSize: Int
    ): Flow<List<MediaEntityView.Core>>

    @Query("""
        select * from media
        where media_type = :mediaType
        and season = :season
        and start_date like :seasonYear
        order by popularity desc
        limit :pageSize
    """)
    @Transaction
    abstract fun popularThisSeasonFlow(
        mediaType: MediaType = MediaType.ANIME,
        pageSize: Int,
        season: MediaSeason,
        seasonYear: FuzzyDateLike
    ): Flow<List<MediaEntityView.Core>>

    @Query("""
        select * from media
        where media_type = :mediaType
        order by id desc
        limit :pageSize
    """)
    @Transaction
    abstract fun recentlyAddedFlow(
        mediaType: MediaType,
        pageSize: Int
    ): Flow<List<MediaEntityView.Core>>

    @Query("""
        select * from media
        where media_type = :mediaType
        and season = :season
        and start_date like :seasonYear
        order by popularity desc
        limit :pageSize
    """)
    @Transaction
    abstract fun anticipatedNextSeasonFlow(
        mediaType: MediaType = MediaType.ANIME,
        pageSize: Int,
        season: MediaSeason,
        seasonYear: FuzzyDateLike
    ): Flow<List<MediaEntityView.Core>>

    @Query("""
        select * from media
        where media_type = :mediaType
        and country_of_origin = :countryCode
        order by popularity desc
        limit :pageSize
    """)
    @Transaction
    abstract fun popularManhwaFlow(
        mediaType: MediaType = MediaType.MANGA,
        countryCode: CountryCode = "KR",
        pageSize: Int
    ): Flow<List<MediaEntityView.Core>>
}
