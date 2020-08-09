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

package co.anitrend.data.source.converters

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.data.source.entity.SourceEntity
import co.anitrend.data.source.model.remote.SourceResult
import co.anitrend.domain.media.contract.IMediaId
import co.anitrend.domain.media.entities.MediaId

internal object SourceModelConverter : SupportConverter<SourceResult, SourceEntity>() {
    /**
     * Function reference from converting from [M] to [E] which will
     * be called by [convertFrom]
     */
    override val fromType: (SourceResult) -> SourceEntity = { model ->
        SourceEntity(
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
    override val toType: (SourceEntity) -> SourceResult = {
        throw NotImplementedError()
    }
}

internal object SourceEntityConverter : SupportConverter<SourceEntity, IMediaId>() {
    /**
     * Function reference from converting from [M] to [E] which will
     * be called by [convertFrom]
     */
    override val fromType: (SourceEntity) -> MediaId = { entity ->
        MediaId(
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
    override val toType: (IMediaId) -> SourceEntity = {
        throw NotImplementedError()
    }
}