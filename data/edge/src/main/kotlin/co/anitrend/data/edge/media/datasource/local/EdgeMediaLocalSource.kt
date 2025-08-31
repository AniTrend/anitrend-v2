/*
 * Copyright (C) 2025 AniTrend
 */
package co.anitrend.data.edge.media.datasource.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import co.anitrend.data.android.source.local.AbstractLocalSource
import co.anitrend.data.edge.media.entity.EdgeMediaEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class EdgeMediaLocalSource : AbstractLocalSource<EdgeMediaEntity>() {
    @Query("select count(id) from edge_media")
    abstract override suspend fun count(): Int

    @Query("delete from edge_media")
    abstract override suspend fun clear()

    @Query("select * from edge_media where id = :id")
    @Transaction
    abstract fun mediaById(id: Int): Flow<EdgeMediaEntity?>
}
