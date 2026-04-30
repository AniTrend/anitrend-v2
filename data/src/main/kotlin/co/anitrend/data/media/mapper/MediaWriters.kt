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
package co.anitrend.data.media.mapper

import co.anitrend.data.android.mapper.PersistEmbedded
import co.anitrend.data.media.datasource.local.MediaLocalSource
import co.anitrend.data.media.entity.MediaEntity

internal fun interface MediaPagedWriterContract {
    suspend fun persist(entities: List<MediaEntity>)
}

internal fun interface MediaDetailWriterContract {
    suspend fun persist(entity: MediaEntity)
}

internal fun interface MediaEmbedWriterContract {
    suspend fun persist(entities: List<MediaEntity>)
}

internal fun interface MediaEmbedWithAiringWriterContract {
    suspend fun persist(entities: List<MediaEntity>)
}

internal fun interface MediaEmbedWithMediaListWriterContract {
    suspend fun persist(entities: List<MediaEntity>)
}

internal class MediaPagedWriter(
    private val localSource: MediaLocalSource,
    private val tagPersistence: PersistEmbedded,
    private val genrePersistence: PersistEmbedded,
    private val airingPersistence: PersistEmbedded,
    private val mediaListPersistence: PersistEmbedded,
) : MediaPagedWriterContract {
    override suspend fun persist(entities: List<MediaEntity>) {
        localSource.upsert(entities)
        tagPersistence.persistEmbedded()
        genrePersistence.persistEmbedded()
        airingPersistence.persistEmbedded()
        mediaListPersistence.persistEmbedded()
    }
}

internal class MediaDetailWriter(
    private val localSource: MediaLocalSource,
    private val linkPersistence: PersistEmbedded,
    private val rankPersistence: PersistEmbedded,
    private val tagPersistence: PersistEmbedded,
    private val genrePersistence: PersistEmbedded,
    private val airingPersistence: PersistEmbedded,
    private val mediaListPersistence: PersistEmbedded,
) : MediaDetailWriterContract {
    override suspend fun persist(entity: MediaEntity) {
        localSource.upsert(entity)
        linkPersistence.persistEmbedded()
        rankPersistence.persistEmbedded()
        tagPersistence.persistEmbedded()
        genrePersistence.persistEmbedded()
        airingPersistence.persistEmbedded()
        mediaListPersistence.persistEmbedded()
    }
}

internal class MediaEmbedWriter(
    private val localSource: MediaLocalSource,
    private val tagPersistence: PersistEmbedded,
    private val genrePersistence: PersistEmbedded,
) : MediaEmbedWriterContract {
    override suspend fun persist(entities: List<MediaEntity>) {
        localSource.upsert(entities)
        tagPersistence.persistEmbedded()
        genrePersistence.persistEmbedded()
    }
}

internal class MediaEmbedWithAiringWriter(
    private val localSource: MediaLocalSource,
    private val tagPersistence: PersistEmbedded,
    private val genrePersistence: PersistEmbedded,
    private val airingPersistence: PersistEmbedded,
) : MediaEmbedWithAiringWriterContract {
    override suspend fun persist(entities: List<MediaEntity>) {
        localSource.upsert(entities)
        tagPersistence.persistEmbedded()
        genrePersistence.persistEmbedded()
        airingPersistence.persistEmbedded()
    }
}

internal class MediaEmbedWithMediaListWriter(
    private val localSource: MediaLocalSource,
    private val tagPersistence: PersistEmbedded,
    private val genrePersistence: PersistEmbedded,
    private val airingPersistence: PersistEmbedded,
    private val mediaListPersistence: PersistEmbedded,
) : MediaEmbedWithMediaListWriterContract {
    override suspend fun persist(entities: List<MediaEntity>) {
        localSource.upsert(entities)
        tagPersistence.persistEmbedded()
        genrePersistence.persistEmbedded()
        airingPersistence.persistEmbedded()
        mediaListPersistence.persistEmbedded()
    }
}
