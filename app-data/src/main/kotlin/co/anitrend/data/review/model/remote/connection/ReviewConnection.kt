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

package co.anitrend.data.review.model.remote.connection

import co.anitrend.data.arch.common.entity.IEntityConnection
import co.anitrend.data.review.model.remote.Review
import co.anitrend.data.review.model.remote.edge.ReviewEdge
import co.anitrend.domain.common.model.PageInfo

data class ReviewConnection(
    override val edges: List<ReviewEdge>?,
    override val nodes: List<Review>?,
    override val pageInfo: PageInfo?
) : IEntityConnection<ReviewEdge, Review>