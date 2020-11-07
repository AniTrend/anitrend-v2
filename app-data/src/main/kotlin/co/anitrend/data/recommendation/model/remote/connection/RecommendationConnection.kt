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

package co.anitrend.data.recommendation.model.remote.connection

import co.anitrend.data.arch.common.entity.IEntityConnection
import co.anitrend.data.arch.common.model.paging.info.PageInfo
import co.anitrend.data.recommendation.model.remote.RecommendationModel
import co.anitrend.data.recommendation.model.remote.edge.RecommendationEdge
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** [RecommendationConnection](https://anilist.github.io/ApiV2-GraphQL-Docs/recommendationconnection.doc.html)
 * Review connection
 */
@Serializable
internal class RecommendationConnection(
    @SerialName("edges") override val edges: List<RecommendationEdge>?,
    @SerialName("nodes") override val nodes: List<RecommendationModel.Core>?,
    @SerialName("pageInfo") override val pageInfo: PageInfo?
) : IEntityConnection<RecommendationEdge, RecommendationModel>