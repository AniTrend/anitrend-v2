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

package co.anitrend.data.studio.converter

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.arch.data.transformer.ISupportTransformer
import co.anitrend.data.studio.entity.StudioEntity
import co.anitrend.data.studio.model.StudioModel
import co.anitrend.domain.studio.entity.Studio

internal class StudioConverter(
    override val fromType: (StudioModel) -> Studio = ::transform,
    override val toType: (Studio) -> StudioModel = { throw NotImplementedError() }
) : SupportConverter<StudioModel, Studio>() {
    private companion object : ISupportTransformer<StudioModel, Studio> {
        override fun transform(source: StudioModel) = when (source) {
            is StudioModel.Core -> Studio.Core(
                favourites = source.favourites ?: 0,
                isAnimationStudio = source.isAnimationStudio,
                isFavourite = source.isFavourite,
                isFavouriteBlocked = source.isFavouriteBlocked ?: false,
                image = null,
                name = source.name,
                siteUrl = source.siteUrl,
                id = source.id
            )
            is StudioModel.Extended -> Studio.Extended(
                favourites = source.favourites ?: 0,
                isAnimationStudio = source.isAnimationStudio,
                isFavourite = source.isFavourite,
                isFavouriteBlocked = source.isFavouriteBlocked ?: false,
                image = null,
                name = source.name,
                siteUrl = source.siteUrl,
                id = source.id
            )
        }
    }
}

internal class StudioModelConverter(
    override val fromType: (StudioModel) -> StudioEntity = ::transform,
    override val toType: (StudioEntity) -> StudioModel = { throw NotImplementedError() }
) : SupportConverter<StudioModel,StudioEntity>() {
    private companion object : ISupportTransformer<StudioModel,StudioEntity> {
        override fun transform(source: StudioModel) = StudioEntity(
            favourites = source.favourites ?: 0,
            isAnimationStudio = source.isAnimationStudio,
            isFavourite = source.isFavourite,
            isFavouriteBlocked = source.isFavouriteBlocked ?: false,
            name = source.name,
            siteUrl = source.siteUrl,
            id = source.id,
        )
    }
}

internal class StudioEntityConverter(
    override val fromType: (StudioEntity) -> Studio = ::transform,
    override val toType: (Studio) -> StudioEntity = { throw NotImplementedError() }
) : SupportConverter<StudioEntity,Studio>() {
    private companion object : ISupportTransformer<StudioEntity,Studio> {
        override fun transform(source: StudioEntity) = Studio.Core(
            favourites = source.favourites,
            isAnimationStudio = source.isAnimationStudio,
            isFavourite = source.isFavourite,
            isFavouriteBlocked = source.isFavouriteBlocked,
            image = null,
            name = source.name,
            siteUrl = source.siteUrl,
            id = source.id
        )
    }
}