/*
 * Copyright (C) 2025 AniTrend
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
package co.anitrend.data.edge.navigation.datasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import co.anitrend.data.android.source.local.AbstractLocalSource
import co.anitrend.data.edge.navigation.entity.EdgeNavigationEntity

@Dao
abstract class EdgeNavigationLocalSource : AbstractLocalSource<EdgeNavigationEntity>() {
    @Query(
        """
        select count(id) from edge_config_navigation
        """,
    )
    abstract override suspend fun count(): Int

    @Query(
        """
        delete from edge_config_navigation
        """,
    )
    abstract override suspend fun clear()

    @Query(
        """
        delete from edge_config_navigation
        where config_id = :configId
        """,
    )
    abstract suspend fun clearByConfigId(configId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertOrReplaceByKey(entities: List<EdgeNavigationEntity>)

    /**
     * Atomically replaces the config's navigation snapshot. The unique config/key index
     * collapses duplicate keys in a single network response.
     */
    @Transaction
    open suspend fun replaceByConfigId(
        configId: Long,
        entities: List<EdgeNavigationEntity>,
    ) {
        clearByConfigId(configId)
        insertOrReplaceByKey(entities)
    }
}
