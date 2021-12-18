/*
 * Copyright (C) 2019  AniTrend
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

package co.anitrend.data.airing.model.query

import co.anitrend.data.common.model.graph.IGraphPayload
import co.anitrend.domain.airing.model.AiringParam

internal data class AiringScheduleQuery(
    val param: AiringParam.Find
) : IGraphPayload {

    /**
     * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
     */
    override fun toMap() = mapOf(
        "id" to param.id,
        "mediaId" to param.mediaId,
        "episode" to param.episode,
        "airingAt" to param.airingAt,
        "notYetAired" to param.notYetAired,
        "id_not" to param.id_not,
        "id_in" to param.id_in,
        "id_not_in" to param.id_not_in,
        "mediaId_not" to param.mediaId_not,
        "mediaId_in" to param.mediaId_in,
        "mediaId_not_in" to param.mediaId_not_in,
        "episode_not" to param.episode_not,
        "episode_in" to param.episode_in,
        "episode_not_in" to param.episode_not_in,
        "episode_greater" to param.episode_greater,
        "episode_lesser" to param.episode_lesser,
        "airingAt_greater" to param.airingAt_greater,
        "airingAt_lesser" to param.airingAt_lesser,
        "sort" to param.sort
    )
}