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
import co.anitrend.data.recommendation.model.remote.RecommendationModel
import co.anitrend.data.recommendation.model.remote.edge.RecommendationEdge
import co.anitrend.domain.common.entity.contract.IEntityPageInfo

/** [RecommendationConnection](https://anilist.github.io/ApiV2-GraphQL-Docs/recommendationconnection.doc.html)
 * Review connection
 */
internal class RecommendationConnection(
    override val edges: List<RecommendationEdge>?,
    override val nodes: List<RecommendationModel>?,
    override val pageInfo: IEntityPageInfo?
) : IEntityConnection<RecommendationEdge, RecommendationModel>