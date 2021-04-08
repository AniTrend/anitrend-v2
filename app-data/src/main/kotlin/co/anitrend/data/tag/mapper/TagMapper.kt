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

package co.anitrend.data.tag.mapper

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.data.android.extensions.runInTransaction
import co.anitrend.data.android.mapper.DefaultMapper
import co.anitrend.data.android.mapper.EmbedMapper
import co.anitrend.data.android.source.AbstractLocalSource
import co.anitrend.data.media.model.MediaModel
import co.anitrend.data.tag.converter.TagModelConverter
import co.anitrend.data.tag.datasource.local.TagLocalSource
import co.anitrend.data.tag.datasource.local.connection.TagConnectionLocalSource
import co.anitrend.data.tag.entity.TagEntity
import co.anitrend.data.tag.entity.connection.TagConnectionEntity
import co.anitrend.data.tag.model.remote.TagContainerModel
import co.anitrend.data.tag.model.remote.TagModel

internal sealed class TagMapper : DefaultMapper<TagContainerModel, List<TagEntity>>() {

    protected abstract val localSource: TagLocalSource
    protected abstract val converter: TagModelConverter

    class Core(
        override val localSource: TagLocalSource,
        override val converter: TagModelConverter
    ) : TagMapper() {

        /**
         * Save [data] into your desired local source
         */
        override suspend fun persist(data: List<TagEntity>) {
            runInTransaction {
                localSource.upsert(data)
            }
        }

        /**
         * Creates mapped objects and handles the database operations which may be required to map various objects
         *
         * @param source the incoming data source type
         * @return Mapped object that will be consumed by [onResponseDatabaseInsert]
         */
        override suspend fun onResponseMapFrom(
            source: TagContainerModel
        ) = converter.convertFrom(
            source.mediaTagCollection
        )
    }

    class Embed(
        override val localSource: TagConnectionLocalSource
    ) : EmbedMapper<Embed.Item, TagConnectionEntity>() {

        override val converter = object : SupportConverter<Item, TagConnectionEntity>() {
            /**
             * Function reference from converting from [M] to [E] which will
             * be called by [convertFrom]
             */
            override val fromType: (Item) -> TagConnectionEntity = {
                TagConnectionEntity(
                    rank = it.rank ?: 0,
                    tagId = it.tagId,
                    mediaId = it.mediaId,
                    isMediaSpoiler = it.isMediaSpoiler ?: false
                )
            }

            /**
             * Function reference from converting from [E] to [M] which will
             * be called by [convertTo]
             */
            override val toType: (TagConnectionEntity) -> Item
                get() = throw NotImplementedError()
        }

        data class Item(
            val rank: Int?,
            val tagId: Long,
            val mediaId: Long,
            val isMediaSpoiler: Boolean?,
        )

        companion object {

            fun asItem(source: MediaModel) =
                source.tags?.map { tag ->
                    Item(
                        rank = tag.rank,
                        tagId = tag.id,
                        mediaId = source.id,
                        isMediaSpoiler = tag.isMediaSpoiler
                    )
                }.orEmpty()

            fun asItem(source: List<MediaModel>) =
                source.flatMap { media ->
                    asItem(media)
                }
        }
    }
}