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

package co.anitrend.data.link.converter

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.arch.data.transformer.ISupportTransformer
import co.anitrend.data.link.entity.LinkEntity
import co.anitrend.data.link.model.LinkModel

internal class LinkModelConverter(
    override val fromType: (LinkModel) -> LinkEntity = ::transform,
    override val toType: (LinkEntity) -> LinkModel = { throw NotImplementedError() }
) : SupportConverter<LinkModel, LinkEntity>() {
    private companion object : ISupportTransformer<LinkModel, LinkEntity> {
        override fun transform(source: LinkModel) = LinkEntity(
            mediaId = 0,
            site = source.site,
            url = source.url,
            id = source.id
        )
    }
}