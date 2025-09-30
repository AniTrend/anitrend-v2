/*
 * Copyright (C) 2025 AniTrend
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
package co.anitrend.data.edge.image.converter

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.data.edge.image.EdgeImageWithMediaId
import co.anitrend.data.edge.image.entity.EdgeMediaImageEntity
import co.anitrend.data.edge.image.model.EdgeImageModel

internal class EdgeImageConverter(
    override val fromType: (EdgeImageWithMediaId) -> EdgeMediaImageEntity = { model ->
        EdgeMediaImageEntity(
            mediaId = model.first,
            type = when (model.second.type) {
                EdgeImageModel.ImageType.BACKDROP -> EdgeMediaImageEntity.ImageType.BACKDROP
                EdgeImageModel.ImageType.LOGO -> EdgeMediaImageEntity.ImageType.LOGO
                EdgeImageModel.ImageType.POSTER -> EdgeMediaImageEntity.ImageType.POSTER
            },
            url = model.second.url,
            height = model.second.height,
            width = model.second.width,
            locale = model.second.locale,
        )
    },
    override val toType: (EdgeMediaImageEntity) -> EdgeImageWithMediaId = { throw NotImplementedError() },
) : SupportConverter<EdgeImageWithMediaId, EdgeMediaImageEntity>()
