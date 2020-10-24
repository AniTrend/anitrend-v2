/*
 * Copyright (C) 2020  AniTrend
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

package co.anitrend.data.moe.converters

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.data.moe.entity.MoeEntity
import co.anitrend.data.moe.model.remote.MoeModel
import co.anitrend.domain.media.entity.attribute.origin.IMediaSourceId
import co.anitrend.domain.media.entity.attribute.origin.MediaSourceId

internal class SourceModelConverter : SupportConverter<MoeModel, MoeEntity>() {
    /**
     * Function reference from converting from [M] to [E] which will
     * be called by [convertFrom]
     */
    override val fromType: (MoeModel) -> MoeEntity = { model ->
        MoeEntity(
            anilist = model.anilist ?: 0,
            aniDb = model.aniDb,
            kitsu = model.kitsu,
            mal = model.mal
        )
    }

    /**
     * Function reference from converting from [E] to [M] which will
     * be called by [convertTo]
     */
    override val toType: (MoeEntity) -> MoeModel = {
        throw NotImplementedError()
    }
}

internal class SourceEntityConverter : SupportConverter<MoeEntity, IMediaSourceId>() {
    /**
     * Function reference from converting from [M] to [E] which will
     * be called by [convertFrom]
     */
    override val fromType: (MoeEntity) -> MediaSourceId = { entity ->
        MediaSourceId.empty().copy(
            anilist = entity.anilist,
            aniDb = entity.aniDb,
            kitsu = entity.kitsu,
            mal = entity.mal
        )
    }

    /**
     * Function reference from converting from [E] to [M] which will
     * be called by [convertTo]
     */
    override val toType: (IMediaSourceId) -> MoeEntity = {
        throw NotImplementedError()
    }
}