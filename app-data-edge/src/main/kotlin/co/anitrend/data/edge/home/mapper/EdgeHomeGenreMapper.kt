/*
 * Copyright (C) 2024 AniTrend
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
package co.anitrend.data.edge.home.mapper

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.data.android.mapper.EmbedMapper
import co.anitrend.data.android.source.local.AbstractLocalSource
import co.anitrend.data.edge.home.entity.EdgeHomeEntity

internal class EdgeHomeGenreMapper(
    override val localSource: AbstractLocalSource<EdgeHomeEntity>,
) : EmbedMapper<EdgeHomeGenreMapper.Item, EdgeHomeEntity>() {
    override val converter =
        object : SupportConverter<Item, EdgeHomeEntity>() {
            /**
             * Function reference from converting from [M] to [E] which will
             * be called by [convertFrom]
             */
            override val fromType: (Item) -> EdgeHomeEntity = {
                EdgeHomeEntity(
                    name = it.name,
                    id = it.id,
                )
            }

            /**
             * Function reference from converting from [E] to [M] which will
             * be called by [convertTo]
             */
            override val toType: (EdgeHomeEntity) -> Item
                get() = throw NotImplementedError()
        }

    data class Item(
        val name: String,
        val configId: Long,
        val id: Long,
    )
}
