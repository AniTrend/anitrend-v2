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

package co.anitrend.data.review.model.remote.edge

import co.anitrend.data.arch.common.entity.IEntityEdge
import co.anitrend.data.review.model.remote.ReviewModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** [ReviewEdge](https://anilist.github.io/ApiV2-GraphQL-Docs/reviewedge.doc.html)
 * Review connection edge
 */
@Serializable
internal data class ReviewEdge(
    @SerialName("id") override val id: Long,
    @SerialName("node") override val node: ReviewModel?
) : IEntityEdge<ReviewModel>