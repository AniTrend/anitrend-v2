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

package co.anitrend.data.tag.datasource.local

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import co.anitrend.data.android.source.local.AbstractLocalSource
import co.anitrend.data.tag.entity.TagEntity
import co.anitrend.data.tag.entity.connection.TagConnectionEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal abstract class TagLocalSource : AbstractLocalSource<TagEntity>() {

    @Query("""
        select count(id) from tag
    """)
    abstract override suspend fun count(): Int

    @Query("""
        delete from tag
    """
    )
    abstract override suspend fun clear()

    @Query("""
        select * from tag
        order by category, name asc
        """
    )
    abstract fun findAllFlow(): Flow<List<TagEntity>>

    @Transaction
    @RawQuery(observedEntities = [TagEntity::class])
    abstract fun rawFlowList(query: SupportSQLiteQuery): Flow<List<TagEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun upsertConnections(attribute: List<TagConnectionEntity>)
}
