/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.data.feed.episode.datasource.local

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import co.anitrend.data.android.source.local.AbstractLocalSource
import co.anitrend.data.feed.episode.entity.EpisodeEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class EpisodeLocalSource : AbstractLocalSource<EpisodeEntity>() {

    @Query("""
        select count(id)
        from episode
    """)
    abstract override suspend fun count(): Int

    @Query("""
        delete from episode
    """)
    abstract override suspend fun clear()

    @Query("""
        select *
        from episode
        where id = :id
        """)
    abstract fun episodeByIdFlow(id: Long): Flow<EpisodeEntity?>

    @Query("""
        select *
        from episode
        where series_title not like '%dub%'
        order by available_premium_time desc, series_title desc,
        length(info_episode_number), info_episode_number
        """)
    abstract fun entryFactory(): DataSource.Factory<Int, EpisodeEntity>

    @Query("""
        select *
        from episode
        where title match :searchTerm or series_title match :searchTerm or description match :searchTerm
        order by available_premium_time desc, series_title desc,
        length(info_episode_number), info_episode_number
        """)
    abstract fun entrySearchFactory(
        searchTerm: String
    ): DataSource.Factory<Int, EpisodeEntity>
}
