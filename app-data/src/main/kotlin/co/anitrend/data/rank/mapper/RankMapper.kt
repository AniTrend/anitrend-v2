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

package co.anitrend.data.rank.mapper

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.data.android.mapper.DefaultMapper
import co.anitrend.data.android.mapper.EmbedMapper
import co.anitrend.data.link.converter.LinkModelConverter
import co.anitrend.data.link.datasource.LinkLocalSource
import co.anitrend.data.link.entity.LinkEntity
import co.anitrend.data.link.mapper.LinkMapper
import co.anitrend.data.link.model.LinkModel
import co.anitrend.data.media.model.MediaModel
import co.anitrend.data.rank.converter.RankModelConverter
import co.anitrend.data.rank.datasource.RankLocalSource
import co.anitrend.data.rank.entity.RankEntity
import co.anitrend.data.rank.model.RankModel

internal sealed class RankMapper : DefaultMapper<List<RankModel>, List<RankEntity>>() {

    protected abstract val localSource: RankLocalSource
    protected abstract val converter: RankModelConverter

    class Core(
        override val localSource: RankLocalSource,
        override val converter: RankModelConverter
    ) : RankMapper() {

        /**
         * Save [data] into your desired local source
         */
        override suspend fun persist(data: List<RankEntity>) {
            localSource.upsert(data)
        }

        /**
         * Creates mapped objects and handles the database operations which may be required to map various objects
         *
         * @param source the incoming data source type
         * @return Mapped object that will be consumed by [onResponseDatabaseInsert]
         */
        override suspend fun onResponseMapFrom(
            source: List<RankModel>
        ) = converter.convertFrom(source)
    }

    class Embed(
        override val localSource: RankLocalSource
    ) : EmbedMapper<Embed.Item, RankEntity>() {

        override val converter = object : SupportConverter<Item, RankEntity>() {
            /**
             * Function reference from converting from [M] to [E] which will
             * be called by [convertFrom]
             */
            override val fromType: (Item) -> RankEntity = {
                RankEntity(
                    mediaId = it.mediaId,
                    allTime = it.rank.allTime,
                    context = it.rank.context,
                    format = it.rank.format,
                    rank = it.rank.rank,
                    season = it.rank.season,
                    type = it.rank.type,
                    year = it.rank.year,
                    id = it.rank.id,
                )
            }

            /**
             * Function reference from converting from [E] to [M] which will
             * be called by [convertTo]
             */
            override val toType: (RankEntity) -> Item
                get() = throw NotImplementedError()

        }

        data class Item(
            val mediaId: Long,
            val rank: RankModel
        )

        companion object {

            fun asItem(source: MediaModel) =
                source.rankings.map { rank ->
                    Item(
                        mediaId = source.id,
                        rank = rank
                    )
                }

            fun asItem(source: List<MediaModel>) =
                source.flatMap { media ->
                    asItem(media)
                }
        }
    }
}