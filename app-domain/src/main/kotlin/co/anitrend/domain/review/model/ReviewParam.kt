/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.domain.review.model

import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.review.enums.ReviewRating

sealed class ReviewParam {

    /** [Review query](https://anilist.github.io/ApiV2-GraphQL-Docs/query.doc.html)
     *
     * @param id Filter by Review id
     * @param mediaId Filter by media id
     * @param userId Filter by media id
     * @param mediaType Filter by media type
     * @param sort The order the results will be returned in
     */
    data class Find(
        val id: Long? = null,
        val mediaId: Long? = null,
        val userId: Long? = null,
        val mediaType: MediaType? = null,
        val sort: List<MediaType>? = null
    ) : ReviewParam()


    /** [RateReview mutation](https://anilist.github.io/ApiV2-GraphQL-Docs/mutation.doc.html)
     *
     * Rate a review
     *
     * @param reviewId The id of the review to rate
     * @param rating The rating to apply to the review
     */
    data class Rate(
        val reviewId: Long,
        val rating: ReviewRating
    ) : ReviewParam()

    /** [DeleteReview mutation](https://anilist.github.io/ApiV2-GraphQL-Docs/mutation.doc.html)
     *
     * Delete a review
     *
     * @param id The id of the review to delete
     */
    data class Delete(
        val id: Long
    ) : ReviewParam()

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
    data class Save(
        val id: Long? = null,
        val mediaId: Long,
        val body: String,
        val summary: String,
        val score: Int,
        val private: Boolean
    ) : ReviewParam() {
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
    }
}
