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
import co.anitrend.data.android.mapper.EmbedMapper
import co.anitrend.data.link.datasource.LinkLocalSource
import co.anitrend.data.link.entity.LinkEntity
import co.anitrend.data.link.model.LinkModel

internal class LinkMapper(
    override val localSource: LinkLocalSource
) : EmbedMapper<LinkMapper.Item, LinkEntity>() {

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
}