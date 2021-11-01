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

package co.anitrend.data.review.mapper

import co.anitrend.data.android.mapper.DefaultMapper
import co.anitrend.data.media.mapper.MediaMapper
import co.anitrend.data.review.converter.ReviewModelConverter
import co.anitrend.data.review.datasource.local.ReviewLocalSource
import co.anitrend.data.review.entity.ReviewEntity
import co.anitrend.data.review.model.container.ReviewContainerModel
import co.anitrend.data.review.model.remote.ReviewModel

internal sealed class ReviewMapper<S, D> : DefaultMapper<S, D>() {

    protected abstract val localSource: ReviewLocalSource
    protected abstract val converter: ReviewModelConverter

    class Entry(
        private val mediaMapper: MediaMapper.EmbedWithAiring,
        override val localSource: ReviewLocalSource,
        override val converter: ReviewModelConverter
    ) : ReviewMapper<ReviewContainerModel.Entry, ReviewEntity>() {
        /**
         * Creates mapped objects and handles the database operations which may be required to map various objects,
         *
         * @param source the incoming data source type
         * @return mapped object that will be consumed by [onResponseDatabaseInsert]
         */
        override suspend fun onResponseMapFrom(source: ReviewContainerModel.Entry): ReviewEntity {
            mediaMapper.onEmbedded(
                source.entry.media
            )
            return converter.convertFrom(source.entry)
        }

        /**
         * Save [data] into your desired local source
         */
        override suspend fun persist(data: ReviewEntity) {
            mediaMapper.persistEmbedded()
            localSource.upsert(data)
        }
    }

    class Paged(
        private val mediaMapper: MediaMapper.EmbedWithAiring,
        override val localSource: ReviewLocalSource,
        override val converter: ReviewModelConverter
    ) : ReviewMapper<ReviewContainerModel.Paged, List<ReviewEntity>>() {
        /**
         * Creates mapped objects and handles the database operations which may be required to map various objects,
         *
         * @param source the incoming data source type
         * @return mapped object that will be consumed by [onResponseDatabaseInsert]
         */
        override suspend fun onResponseMapFrom(source: ReviewContainerModel.Paged): List<ReviewEntity> {
            mediaMapper.onEmbedded(
                source.page.entries.map(
                    ReviewModel.Extended::media
                )
            )
            return converter.convertFrom(source.page.entries)
        }

        /**
         * Save [data] into your desired local source
         */
        override suspend fun persist(data: List<ReviewEntity>) {
            mediaMapper.persistEmbedded()
            localSource.upsert(data)
        }
    }

    class Rate(
        override val localSource: ReviewLocalSource,
        override val converter: ReviewModelConverter
    ) : ReviewMapper<ReviewContainerModel.RatedEntry, ReviewEntity>() {
        /**
         * Creates mapped objects and handles the database operations which may be required to map various objects,
         *
         * @param source the incoming data source type
         * @return mapped object that will be consumed by [onResponseDatabaseInsert]
         */
        override suspend fun onResponseMapFrom(
            source: ReviewContainerModel.RatedEntry
        ) = converter.convertFrom(source.entry)

        /**
         * Save [data] into your desired local source
         */
        override suspend fun persist(data: ReviewEntity) {
            localSource.upsert(data)
        }
    }

    class Save(
        override val localSource: ReviewLocalSource,
        override val converter: ReviewModelConverter
    ) : ReviewMapper<ReviewContainerModel.SavedEntry, ReviewEntity>() {
        /**
         * Creates mapped objects and handles the database operations which may be required to map various objects,
         *
         * @param source the incoming data source type
         * @return mapped object that will be consumed by [onResponseDatabaseInsert]
         */
        override suspend fun onResponseMapFrom(
            source: ReviewContainerModel.SavedEntry
        ) = converter.convertFrom(source.entry)

        /**
         * Save [data] into your desired local source
         */
        override suspend fun persist(data: ReviewEntity) {
            localSource.upsert(data)
        }
    }

    class Delete(
        override val localSource: ReviewLocalSource,
        override val converter: ReviewModelConverter
    ) : ReviewMapper<ReviewContainerModel.DeletedEntry, Boolean>() {
        /**
         * Creates mapped objects and handles the database operations which may be required to map various objects,
         *
         * @param source the incoming data source type
         * @return mapped object that will be consumed by [onResponseDatabaseInsert]
         */
        override suspend fun onResponseMapFrom(
            source: ReviewContainerModel.DeletedEntry
        ) = source.entry.deleted

        /**
         * Save [data] into your desired local source
         */
        override suspend fun persist(data: Boolean) {

        }
    }
}