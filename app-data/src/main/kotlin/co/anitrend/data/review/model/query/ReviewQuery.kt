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

package co.anitrend.data.review.model.query

import co.anitrend.domain.common.graph.IGraphPayload
import co.anitrend.domain.media.enums.MediaType
import kotlinx.android.parcel.Parcelize

/** [Review query](https://anilist.github.io/ApiV2-GraphQL-Docs/query.doc.html)
 *
 * @param id Filter by Review id
 * @param mediaId Filter by media id
 * @param userId Filter by media id
 * @param mediaType Filter by media type
 * @param sort The order the results will be returned in
 */
@Parcelize
data class ReviewQuery(
    val id: Long? = null,
    val mediaId: Long? = null,
    val userId: Long? = null,
    val mediaType: MediaType? = null,
    val sort: List<MediaType>? = null
) : IGraphPayload {

    /**
     * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
     */
    override fun toMap() = mapOf(
        "id" to id,
        "mediaId" to mediaId,
        "userId" to userId,
        "mediaType" to mediaType,
        "sort" to sort
    )
}