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

package co.anitrend.data.airing.model.connection

import co.anitrend.data.airing.model.AiringScheduleModel
import co.anitrend.data.airing.model.edge.AiringScheduleEdge
import co.anitrend.data.arch.common.entity.IEntityConnection
import co.anitrend.domain.common.entity.shared.PageInfo
import com.google.gson.annotations.SerializedName

/**
 * Airing schedule connection
 */
internal data class AiringScheduleConnection(
    @SerializedName("edges") override val edges: List<AiringScheduleEdge>?,
    @SerializedName("nodes") override val nodes: List<AiringScheduleModel>?,
    @SerializedName("pageInfo") override val pageInfo: PageInfo?
) : IEntityConnection<AiringScheduleEdge, AiringScheduleModel>