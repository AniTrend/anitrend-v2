/*
 * Copyright (C) 2026 AniTrend
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
package co.anitrend.data.medialist.mapper

import co.anitrend.data.android.mapper.PersistEmbedded
import co.anitrend.data.medialist.datasource.local.MediaListLocalSource
import co.anitrend.data.medialist.entity.MediaListEntity

internal interface MediaListWriterContract {
    suspend fun persist(entity: MediaListEntity)

    suspend fun persist(entities: List<MediaListEntity>)
}

internal fun interface MediaListEmbedWriterContract {
    suspend fun persist(entities: List<MediaListEntity>)
}

internal fun interface MediaListEmbedWithMediaWriterContract {
    suspend fun persist(entities: List<MediaListEntity>)
}

internal class MediaListWriter(
    private val userPersistence: PersistEmbedded,
    private val mediaPersistence: PersistEmbedded,
    private val localSource: MediaListLocalSource,
    private val customListPersistence: PersistEmbedded,
    private val customScorePersistence: PersistEmbedded,
) : MediaListWriterContract {
    override suspend fun persist(entity: MediaListEntity) {
        userPersistence.persistEmbedded()
        mediaPersistence.persistEmbedded()
        localSource.upsert(entity)
        customListPersistence.persistEmbedded()
        customScorePersistence.persistEmbedded()
    }

    override suspend fun persist(entities: List<MediaListEntity>) {
        userPersistence.persistEmbedded()
        mediaPersistence.persistEmbedded()
        localSource.upsert(entities)
        customListPersistence.persistEmbedded()
        customScorePersistence.persistEmbedded()
    }
}

internal class MediaListEmbedWriter(
    private val userPersistence: PersistEmbedded,
    private val localSource: MediaListLocalSource,
    private val customListPersistence: PersistEmbedded,
    private val customScorePersistence: PersistEmbedded,
) : MediaListEmbedWriterContract {
    override suspend fun persist(entities: List<MediaListEntity>) {
        localSource.upsert(entities)
        userPersistence.persistEmbedded()
        customListPersistence.persistEmbedded()
        customScorePersistence.persistEmbedded()
    }
}

internal class MediaListEmbedWithMediaWriter(
    private val userPersistence: PersistEmbedded,
    private val mediaPersistence: PersistEmbedded,
    private val localSource: MediaListLocalSource,
) : MediaListEmbedWithMediaWriterContract {
    override suspend fun persist(entities: List<MediaListEntity>) {
        userPersistence.persistEmbedded()
        mediaPersistence.persistEmbedded()
        localSource.upsert(entities)
    }
}
