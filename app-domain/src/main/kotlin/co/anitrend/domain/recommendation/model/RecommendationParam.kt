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

package co.anitrend.domain.recommendation.model

import co.anitrend.domain.recommendation.enums.RecommendationRating
import co.anitrend.domain.recommendation.enums.RecommendationSort

sealed class RecommendationParam {

    /** [Recommendation query](https://anilist.github.io/ApiV2-GraphQL-Docs/query.doc.html)
     *
     * @param id Filter by recommendation id
     * @param mediaId Filter by media id
     * @param mediaRecommendationId Filter by media recommendation id
     * @param onList Filter by the media on the authenticated user's lists
     * @param rating Filter by total rating of the recommendation
     * @param rating_greater Filter by total rating of the recommendation
     * @param rating_lesser Filter by total rating of the recommendation
     * @param sort The order the results will be returned in
     * @param userId Filter by user who created the recommendation
     */
    data class Find(
        val id: Int? = null,
        val mediaId: Int? = null,
        val mediaRecommendationId: Int? = null,
        val onList: Boolean? = null,
        val rating: Int? = null,
        val rating_greater: Int? = null,
        val rating_lesser: Int? = null,
        val sort: List<RecommendationSort>? = null,
        val userId: Int? = null
    ) : RecommendationParam()

    /** [SaveRecommendation mutation](https://anilist.github.io/ApiV2-GraphQL-Docs/mutation.doc.html)
     *
     * Recommendation a media
     *
     * @param mediaId The id of the base media
     * @param mediaRecommendationId The id of the media to recommend
     * @param rating The rating to give the recommendation
     */
    data class Save(
        val mediaId: Int,
        val mediaRecommendationId: Int,
        val rating: RecommendationRating
    ) : RecommendationParam()
}
