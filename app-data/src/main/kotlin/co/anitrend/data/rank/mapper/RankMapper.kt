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
import co.anitrend.data.android.mapper.EmbedMapper
import co.anitrend.data.rank.datasource.RankLocalSource
import co.anitrend.data.rank.entity.RankEntity
import co.anitrend.data.rank.model.RankModel

internal class RankMapper(
    override val localSource: RankLocalSource
) : EmbedMapper<RankMapper.Item, RankEntity>() {

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
}