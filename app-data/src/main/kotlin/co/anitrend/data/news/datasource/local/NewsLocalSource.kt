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

package co.anitrend.data.news.datasource.local

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import co.anitrend.data.arch.database.dao.ILocalSource
import co.anitrend.data.news.entity.NewsEntity

@Dao
internal abstract class NewsLocalSource : ILocalSource<NewsEntity> {
    @Query("""
        select count(id)
        from news
    """)
    abstract override suspend fun count(): Int

    @Query("""
        delete from news
    """)
    abstract override suspend fun clear()

    @Query("""
        select * 
        from news 
        order by published_on desc
        """)
    abstract fun entryFactory(): DataSource.Factory<Int, NewsEntity>

    @Query("""
        select * 
        from news 
        where title match :searchTerm or description match :searchTerm or sub_title match :searchTerm
        order by published_on desc
        """)
    abstract fun entrySearchFactory(
        searchTerm: String
    ): DataSource.Factory<Int, NewsEntity>
}