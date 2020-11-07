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
import co.anitrend.data.airing.entity.AiringScheduleEntity
import co.anitrend.data.airing.model.AiringScheduleModel
import co.anitrend.domain.airing.entity.AiringSchedule

internal class AiringConverter(
    override val fromType: (AiringScheduleModel) -> AiringSchedule = { from(it) },
    override val toType: (AiringSchedule) -> AiringScheduleModel = { throw NotImplementedError() }
) : SupportConverter<AiringScheduleModel, AiringSchedule>() {
    companion object {
        private fun from(source: AiringScheduleModel) = AiringSchedule(
            airingAt = source.airingAt,
            episode = source.episode,
            mediaId = source.mediaId,
            timeUntilAiring = source.timeUntilAiring,
            id = source.id
        )
    }
}

internal class AiringModelConverter(
    override val fromType: (AiringScheduleModel) -> AiringScheduleEntity = { from(it) },
    override val toType: (AiringScheduleEntity) -> AiringScheduleModel = { throw NotImplementedError() }
) : SupportConverter<AiringScheduleModel, AiringScheduleEntity>() {
    companion object {
        private fun from(source: AiringScheduleModel) = AiringScheduleEntity(
            airingAt = source.airingAt,
            episode = source.episode,
            mediaId = source.mediaId,
            timeUntilAiring = source.timeUntilAiring,
            id = source.id
        )
    }
}

internal class AiringExtendedModelConverter(
    override val fromType: (AiringScheduleModel.Extended) -> AiringScheduleEntity = { from(it) },
    override val toType: (AiringScheduleEntity) -> AiringScheduleModel.Extended = { throw NotImplementedError() }
) : SupportConverter<AiringScheduleModel.Extended, AiringScheduleEntity>() {
    companion object {
        private fun from(source: AiringScheduleModel.Extended) = AiringScheduleEntity(
            airingAt = source.airingAt,
            episode = source.episode,
            mediaId = source.mediaId,
            timeUntilAiring = source.timeUntilAiring,
            id = source.id
        )
    }
}

internal class AiringEntityConverter(
    override val fromType: (AiringScheduleEntity) -> AiringSchedule = { from(it) },
    override val toType: (AiringSchedule) -> AiringScheduleEntity = { throw NotImplementedError() }
) : SupportConverter<AiringScheduleEntity, AiringSchedule>() {
    companion object {
        private fun from(source: AiringScheduleEntity) = AiringSchedule(
            airingAt = source.airingAt,
            episode = source.episode,
            mediaId = source.mediaId,
            timeUntilAiring = source.timeUntilAiring,
            id = source.id
        )
    }
}