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

package co.anitrend.domain.entities.mutation.review

import co.anitrend.domain.common.graph.IGraphPayload

/** [SaveReview mutation](https://anilist.github.io/ApiV2-GraphQL-Docs/mutation.doc.html)
 *
 * Create or update a review
 *
 * @param id The review id, required for updating
 * @param mediaId The id of the media the review is of
 * @param body The main review text.
 * @param summary A short summary/preview of the review.
 * @param score Score of the show
 * @param private If the review should only be visible to its creator
 */
data class SaveReviewMutation(
    val id: Long? = null,
    val mediaId: Long,
    val body: String,
    val summary: String,
    val score: Int,
    val private: Boolean
) : IGraphPayload {

    /**
     * Summary validation Min:20, Max:120 characters
     *
     * @see summary
     */
    fun isSummaryValid() = summary.length in 20..120

    /**
     * Body validation Min:1500 characters
     *
     * @see body
     */
    fun isBodyValid() = body.length >= 1500

    /**
     * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
     */
    override fun toMap() = mapOf(
        "id" to id,
        "mediaId" to mediaId,
        "body" to body,
        "summary" to summary,
        "score" to score,
        "private" to private
    )
}