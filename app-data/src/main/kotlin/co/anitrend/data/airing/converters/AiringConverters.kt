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

package co.anitrend.data.airing.converters

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.arch.data.transformer.ISupportTransformer
import co.anitrend.data.airing.entity.AiringScheduleEntity
import co.anitrend.data.airing.model.AiringScheduleModel
import co.anitrend.data.airing.model.contract.IAiringScheduleModel
import co.anitrend.domain.airing.entity.AiringSchedule

internal class AiringConverter(
    override val fromType: (IAiringScheduleModel) -> AiringSchedule = ::transform,
    override val toType: (AiringSchedule) -> IAiringScheduleModel = { throw NotImplementedError() }
) : SupportConverter<IAiringScheduleModel, AiringSchedule>() {
    private companion object : ISupportTransformer<IAiringScheduleModel, AiringSchedule> {
        override fun transform(source: IAiringScheduleModel) = AiringSchedule(
            airingAt = source.airingAt,
            episode = source.episode,
            mediaId = source.mediaId,
            timeUntilAiring = source.timeUntilAiring,
            id = source.id
        )
    }
}

internal class AiringModelConverter(
    override val fromType: (AiringScheduleModel) -> AiringScheduleEntity = ::transform,
    override val toType: (AiringScheduleEntity) -> AiringScheduleModel = { throw NotImplementedError() }
) : SupportConverter<AiringScheduleModel, AiringScheduleEntity>() {
    private companion object : ISupportTransformer<AiringScheduleModel, AiringScheduleEntity> {
        override fun transform(source: AiringScheduleModel) = when (source) {
            is AiringScheduleModel.Core -> AiringScheduleEntity(
                airingAt = source.airingAt,
                episode = source.episode,
                mediaId = source.mediaId,
                timeUntilAiring = source.timeUntilAiring,
                id = source.id
            )
            is AiringScheduleModel.Extended -> AiringScheduleEntity(
                airingAt = source.airingAt,
                episode = source.episode,
                mediaId = source.mediaId,
                timeUntilAiring = source.timeUntilAiring,
                id = source.id
            )
        }
    }
}

internal class AiringEntityConverter(
    override val fromType: (AiringScheduleEntity) -> AiringSchedule = ::transform,
    override val toType: (AiringSchedule) -> AiringScheduleEntity = { throw NotImplementedError() }
) : SupportConverter<AiringScheduleEntity, AiringSchedule>() {
    private companion object : ISupportTransformer<AiringScheduleEntity, AiringSchedule> {
        override fun transform(source: AiringScheduleEntity) = AiringSchedule(
            airingAt = source.airingAt,
            episode = source.episode,
            mediaId = source.mediaId,
            timeUntilAiring = source.timeUntilAiring,
            id = source.id
        )
    }
}