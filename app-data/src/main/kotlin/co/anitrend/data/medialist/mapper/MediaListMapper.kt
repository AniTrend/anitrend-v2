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

import co.anitrend.data.android.extensions.runInTransaction
import co.anitrend.data.android.mapper.DefaultMapper
import co.anitrend.data.android.mapper.EmbedMapper
import co.anitrend.data.common.model.delete.DeletedModel
import co.anitrend.data.customlist.mapper.CustomListMapper
import co.anitrend.data.customscore.mapper.CustomScoreMapper
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

    class Deleted(
        override val localSource: MediaListLocalSource,
        override val converter: MediaListModelConverter,
    ) : MediaListMapper<MediaListContainerModel.Deleted, Boolean>() {

        /**
         * Save [data] into your desired local source
         */
        override suspend fun persist(data: Boolean) {

        }

        /**
         * Creates mapped objects and handles the database operations which may be required to map various objects,
         *
         * @param source the incoming data source type
         * @return mapped object that will be consumed by [onResponseDatabaseInsert]
         */
        override suspend fun onResponseMapFrom(
            source: MediaListContainerModel.Deleted
        ) = source.entry.deleted
    }

    class Single(
        private val mediaMapper: MediaMapper.EmbedWithAiring,
        private val customListMapper: CustomListMapper,
        private val customScoreMapper: CustomScoreMapper,
        override val localSource: MediaListLocalSource,
        override val converter: MediaListModelConverter,
    ) : MediaListMapper<MediaListContainerModel.Single, MediaListEntity>() {

        /**
         * Save [data] into your desired local source
         */
        override suspend fun persist(data: MediaListEntity) {
            mediaMapper.persistEmbedded()
            localSource.upsert(data)
            customListMapper.persistEmbedded()
            customScoreMapper.persistEmbedded()
        }

        /**
         * Creates mapped objects and handles the database operations which may be required to map various objects,
         *
         * @param source the incoming data source type
         * @return mapped object that will be consumed by [onResponseDatabaseInsert]
         */
        override suspend fun onResponseMapFrom(source: MediaListContainerModel.Single): MediaListEntity {
            mediaMapper.onEmbedded(
                source.entry.media
            )
            customListMapper.onEmbedded(
                CustomListMapper.asItem(source.entry)
            )
            customScoreMapper.onEmbedded(
                CustomScoreMapper.asItem(source.entry)
            )
            return converter.convertFrom(source.entry)
        }
    }

    class Many(
        private val mediaMapper: MediaMapper.EmbedWithAiring,
        private val customListMapper: CustomListMapper,
        private val customScoreMapper: CustomScoreMapper,
        override val localSource: MediaListLocalSource,
        override val converter: MediaListModelConverter,
    ) : MediaListMapper<MediaListContainerModel.Many, List<MediaListEntity>>() {

        /**
         * Save [data] into your desired local source
         */
        override suspend fun persist(data: List<MediaListEntity>) {
            mediaMapper.persistEmbedded()
            localSource.upsert(data)
            customListMapper.persistEmbedded()
            customScoreMapper.persistEmbedded()
        }

        /**
         * Creates mapped objects and handles the database operations which may be required to map various objects,
         *
         * @param source the incoming data source type
         * @return mapped object that will be consumed by [onResponseDatabaseInsert]
         */
        override suspend fun onResponseMapFrom(source: MediaListContainerModel.Many): List<MediaListEntity> {
            mediaMapper.onEmbedded(
                source.entries.map(
                    MediaListModel.Extended::media
                )
            )
            customListMapper.onEmbedded(
                CustomListMapper.asItem(source.entries)
            )
            customScoreMapper.onEmbedded(
                CustomScoreMapper.asItem(source.entries)
            )
            return converter.convertFrom(source.entries)
        }
    }

    class Collection(
        private val mediaMapper: MediaMapper.EmbedWithAiring,
        private val customListMapper: CustomListMapper,
        private val customScoreMapper: CustomScoreMapper,
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
                mediaMapper.persistEmbedded()
                localSource.upsert(data)
                customListMapper.persistEmbedded()
                customScoreMapper.persistEmbedded()
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
            val mediaList = source.lists.flatMap { it.entries }
            mediaMapper.onEmbedded(
                mediaList.map(MediaListModel.Extended::media)
            )
            customListMapper.onEmbedded(
                CustomListMapper.asItem(mediaList)
            )
            customScoreMapper.onEmbedded(
                CustomScoreMapper.asItem(mediaList)
            )
            return converter.convertFrom(mediaList)
        }
    }

    class Paged(
        private val customListMapper: CustomListMapper,
        private val customScoreMapper: CustomScoreMapper,
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
                customListMapper.persistEmbedded()
                customScoreMapper.persistEmbedded()
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
                source.page.entries.map(
                    MediaListModel.Extended::media
                )
            )
            customListMapper.onEmbedded(
                CustomListMapper.asItem(source.page.entries)
            )
            customScoreMapper.onEmbedded(
                CustomScoreMapper.asItem(source.page.entries)
            )
            return converter.convertFrom(source.page.entries)
        }
    }

    class Embed(
        private val customListMapper: CustomListMapper,
        private val customScoreMapper: CustomScoreMapper,
        override val localSource: MediaListLocalSource,
        override val converter: MediaListModelConverter
    ) : EmbedMapper<MediaListModel, MediaListEntity>() {
        /**
         * Save [data] into your desired local source
         */
        override suspend fun persist(data: List<MediaListEntity>) {
            super.persist(data)
            customListMapper.persistEmbedded()
            customScoreMapper.persistEmbedded()
        }

        /**
         * Creates mapped objects and handles the database operations which may be required to map various objects,
         *
         * @param source the incoming data source type
         * @return mapped object that will be consumed by [onResponseDatabaseInsert]
         */
        override suspend fun onResponseMapFrom(source: List<MediaListModel>): List<MediaListEntity> {
            customListMapper.onEmbedded(
                CustomListMapper.asItem(source)
            )
            customScoreMapper.onEmbedded(
                CustomScoreMapper.asItem(source)
            )
            return super.onResponseMapFrom(source)
        }
    }

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