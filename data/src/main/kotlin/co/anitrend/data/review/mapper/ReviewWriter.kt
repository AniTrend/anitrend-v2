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
package co.anitrend.data.review.mapper

import co.anitrend.data.android.mapper.PersistEmbedded
import co.anitrend.data.review.datasource.local.ReviewLocalSource
import co.anitrend.data.review.entity.ReviewEntity

internal interface ReviewWriterContract {
    suspend fun persist(entity: ReviewEntity)

    suspend fun persist(entities: List<ReviewEntity>)
}

internal class ReviewWriter(
    private val mediaPersistence: PersistEmbedded,
    private val userPersistence: PersistEmbedded,
    private val localSource: ReviewLocalSource,
) : ReviewWriterContract {
    override suspend fun persist(entity: ReviewEntity) {
        mediaPersistence.persistEmbedded()
        localSource.upsert(entity)
    }

    override suspend fun persist(entities: List<ReviewEntity>) {
        mediaPersistence.persistEmbedded()
        userPersistence.persistEmbedded()
        localSource.upsert(entities)
    }
}
