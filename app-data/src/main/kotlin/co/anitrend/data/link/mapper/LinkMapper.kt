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

package co.anitrend.data.link.mapper

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.data.android.mapper.DefaultMapper
import co.anitrend.data.android.mapper.EmbedMapper
import co.anitrend.data.link.converter.LinkModelConverter
import co.anitrend.data.link.datasource.LinkLocalSource
import co.anitrend.data.link.entity.LinkEntity
import co.anitrend.data.link.model.LinkModel
import co.anitrend.data.media.model.MediaModel

internal sealed class LinkMapper : DefaultMapper<List<LinkModel>, List<LinkEntity>>() {

    protected abstract val localSource: LinkLocalSource
    protected abstract val converter: LinkModelConverter

    class Core(
        override val localSource: LinkLocalSource,
        override val converter: LinkModelConverter
    ) : LinkMapper() {

        /**
         * Save [data] into your desired local source
         */
        override suspend fun persist(data: List<LinkEntity>) {
            localSource.upsert(data)
        }

        /**
         * Creates mapped objects and handles the database operations which may be required to map various objects
         *
         * @param source the incoming data source type
         * @return Mapped object that will be consumed by [onResponseDatabaseInsert]
         */
        override suspend fun onResponseMapFrom(
            source: List<LinkModel>
        ) = converter.convertFrom(source)
    }

    class Embed(
        override val localSource: LinkLocalSource
    ) : EmbedMapper<Embed.Item, LinkEntity>() {

        override val converter = object : SupportConverter<Item, LinkEntity>() {
            /**
             * Function reference from converting from [M] to [E] which will
             * be called by [convertFrom]
             */
            override val fromType: (Item) -> LinkEntity = {
                LinkEntity(
                    mediaId = it.mediaId,
                    site = it.link.site,
                    url = it.link.url,
                    id = it.link.id,
                )
            }

            /**
             * Function reference from converting from [E] to [M] which will
             * be called by [convertTo]
             */
            override val toType: (LinkEntity) -> Item
                get() = throw NotImplementedError()
        }

        data class Item(
            val mediaId: Long,
            val link: LinkModel
        )

        companion object {

            fun asItem(source: MediaModel) =
                source.externalLinks.map { link ->
                    Item(
                        mediaId = source.id,
                        link = link
                    )
                }

            fun asItem(source: List<MediaModel>) =
                source.flatMap { media ->
                    asItem(media)
                }
        }
    }
}