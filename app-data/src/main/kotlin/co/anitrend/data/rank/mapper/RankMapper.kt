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

package co.anitrend.data.rank.mapper

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.data.arch.mapper.EmbedMapper
import co.anitrend.data.rank.datasource.RankLocalSource
import co.anitrend.data.rank.entity.RankEntity
import co.anitrend.data.rank.model.RankModel

internal class RankMapper(
    override val localSource: RankLocalSource
) : EmbedMapper<RankModel, RankEntity>() {

    var mediaId: Long = 0

    override val converter = object : SupportConverter<RankModel, RankEntity>() {
        /**
         * Function reference from converting from [M] to [E] which will
         * be called by [convertFrom]
         */
        override val fromType: (RankModel) -> RankEntity = {
            RankEntity(
                mediaId = mediaId,
                allTime = it.allTime,
                context = it.context,
                format = it.format,
                rank = it.rank,
                season = it.season,
                type = it.type,
                year = it.year,
                id = it.id,
            )
        }

        /**
         * Function reference from converting from [E] to [M] which will
         * be called by [convertTo]
         */
        override val toType: (RankEntity) -> RankModel
                get() = throw NotImplementedError()

    }
}