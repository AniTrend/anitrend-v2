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
package co.anitrend.data.edge.media.mapper

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.data.edge.media.entity.EdgeMediaEntity
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.attribute.image.MediaImage
import co.anitrend.domain.media.entity.attribute.title.MediaTitle

/**
 * Converts Edge media entities to a minimal domain Media projection.
 * This stays within data layer boundaries and is injected where needed.
 */
internal class EdgeMediaEntityConverter : SupportConverter<EdgeMediaEntity, Media>() {
    override val fromType: (EdgeMediaEntity) -> Media = { entity ->
        Media.Core.empty().copy(
            id = entity.id.toLong(),
            title =
                MediaTitle(
                    romaji = entity.title.romaji,
                    english = entity.title.english,
                    native = entity.title.native,
                    userPreferred = null,
                ),
            image =
                MediaImage(
                    color = entity.cover.color,
                    extraLarge = entity.cover.extraLarge,
                    large = entity.cover.large,
                    medium = entity.cover.medium,
                    banner = entity.bannerImage,
                ),
        )
    }

    override val toType: (Media) -> EdgeMediaEntity = { _ ->
        throw NotImplementedError()
    }
}
