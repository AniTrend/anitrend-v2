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

package co.anitrend.data.rank.converter

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.arch.data.transformer.ISupportTransformer
import co.anitrend.data.rank.entity.RankEntity
import co.anitrend.data.rank.model.RankModel

internal class RankModelConverter(
    override val fromType: (RankModel) -> RankEntity = ::transform,
    override val toType: (RankEntity) -> RankModel = { throw NotImplementedError() }
) : SupportConverter<RankModel, RankEntity>() {
    private companion object : ISupportTransformer<RankModel, RankEntity> {
        override fun transform(source: RankModel) = RankEntity(
            mediaId = 0,
            allTime = source.allTime,
            context = source.context,
            format = source.format,
            rank = source.rank,
            season = source.season,
            type = source.type,
            year = source.year,
            id = source.id
        )
    }
}
