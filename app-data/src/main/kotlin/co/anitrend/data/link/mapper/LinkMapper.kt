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
import co.anitrend.data.arch.mapper.EmbedMapper
import co.anitrend.data.link.datasource.LinkLocalSource
import co.anitrend.data.link.entity.LinkEntity
import co.anitrend.data.link.model.LinkModel

internal class LinkMapper(
    override val localSource: LinkLocalSource
) : EmbedMapper<LinkModel, LinkEntity>() {

    var mediaId: Long = 0

    override val converter = object : SupportConverter<LinkModel, LinkEntity>() {
        /**
         * Function reference from converting from [M] to [E] which will
         * be called by [convertFrom]
         */
        override val fromType: (LinkModel) -> LinkEntity = {
            LinkEntity(
                mediaId = mediaId,
                site = it.site,
                url = it.url,
                id = it.id,
            )
        }

        /**
         * Function reference from converting from [E] to [M] which will
         * be called by [convertTo]
         */
        override val toType: (LinkEntity) -> LinkModel = throw NotImplementedError()
    }
}