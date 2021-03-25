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

package co.anitrend.data.medialist.mapper

import co.anitrend.data.arch.database.extensions.runInTransaction
import co.anitrend.data.arch.mapper.DefaultMapper
import co.anitrend.data.arch.mapper.EmbedMapper
import co.anitrend.data.media.mapper.MediaMapper
import co.anitrend.data.medialist.converter.MediaListModelConverter
import co.anitrend.data.medialist.datasource.local.MediaListLocalSource
import co.anitrend.data.medialist.entity.MediaListEntity
import co.anitrend.data.medialist.model.MediaListModel
import co.anitrend.data.medialist.model.container.MediaListContainerModel
import co.anitrend.data.user.mapper.UserMapper

internal sealed class MediaListMapper<S, D> : DefaultMapper<S, D>() {

    protected abstract val localSource: MediaListLocalSource
    protected abstract val converter: MediaListModelConverter

    class Collection(
        private val userMapper: UserMapper.Embed,
        override val localSource: MediaListLocalSource,
        override val converter: MediaListModelConverter,
    ) : MediaListMapper<MediaListContainerModel.Collection, List<MediaListEntity>>() {

        /**
         * Save [data] into your desired local source
         */
        override suspend fun persist(data: List<MediaListEntity>) {
            runInTransaction {
                userMapper.persistEmbedded()
                localSource.upsert(data)
            }
        }

        /**
         * Creates mapped objects and handles the database operations which may be required to map various objects,
         *
         * @param source the incoming data source type
         * @return mapped object that will be consumed by [onResponseDatabaseInsert]
         */
        override suspend fun onResponseMapFrom(source: MediaListContainerModel.Collection): List<MediaListEntity> {
            userMapper.onEmbedded(source.user)
            val mediaList = source.lists.flatMap { it.entries.orEmpty() }
            return converter.convertFrom(mediaList)
        }
    }

    class Paged(
        private val mediaMapper: MediaMapper.EmbedWithAiring,
        override val localSource: MediaListLocalSource,
        override val converter: MediaListModelConverter,
    ) : MediaListMapper<MediaListContainerModel.Paged, List<MediaListEntity>>() {

        /**
         * Save [data] into your desired local source
         */
        override suspend fun persist(data: List<MediaListEntity>) {
            runInTransaction {
                mediaMapper.persistEmbedded()
                localSource.upsert(data)
            }
        }

        /**
         * Creates mapped objects and handles the database operations which may be required to map various objects,
         *
         * @param source the incoming data source type
         * @return mapped object that will be consumed by [onResponseDatabaseInsert]
         */
        override suspend fun onResponseMapFrom(source: MediaListContainerModel.Paged): List<MediaListEntity> {
            mediaMapper.onEmbedded(
                source.page.mediaList.map(
                    MediaListModel.Extended::media
                )
            )
            return converter.convertFrom(source.page.mediaList)
        }
    }

    class Embed(
        override val localSource: MediaListLocalSource,
        override val converter: MediaListModelConverter
    ) : EmbedMapper<MediaListModel, MediaListEntity>()

    class EmbedWithMedia(
        private val mediaMapper: MediaMapper.EmbedWithAiring,
        override val localSource: MediaListLocalSource,
        override val converter: MediaListModelConverter
    ) : EmbedMapper<MediaListModel, MediaListEntity>() {

        /**
         * Save [data] into your desired local source
         */
        override suspend fun persist(data: List<MediaListEntity>) {
            mediaMapper.persistEmbedded()
            super.persist(data)
        }

        /**
         * Creates mapped objects and handles the database operations which may be required to map various objects,
         *
         * @param source the incoming data source type
         * @return mapped object that will be consumed by [onResponseDatabaseInsert]
         */
        override suspend fun onResponseMapFrom(source: List<MediaListModel>): List<MediaListEntity> {
            mediaMapper.onEmbedded(
                source.mapNotNull {
                    if (it is MediaListModel.Extended)
                        it.media
                    else null
                }
            )
            return super.onResponseMapFrom(source)
        }
    }
}