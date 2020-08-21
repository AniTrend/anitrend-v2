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

package co.anitrend.domain.recommendation.model.mutation

import co.anitrend.domain.common.graph.IGraphPayload
import co.anitrend.domain.recommendation.enums.RecommendationRating

/** [SaveRecommendation mutation](https://anilist.github.io/ApiV2-GraphQL-Docs/mutation.doc.html)
 *
 * Recommendation a media
 *
 * @param mediaId The id of the base media
 * @param mediaRecommendationId The id of the media to recommend
 * @param rating The rating to give the recommendation
 */
data class SaveRecommendationMutation(
    val mediaId: Int,
    val mediaRecommendationId: Int,
    val rating: RecommendationRating
) : IGraphPayload {

    /**
     * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
     */
    override fun toMap() = mapOf(
        "mediaId" to mediaId,
        "mediaRecommendationId" to mediaRecommendationId,
        "rating" to rating
    )
}