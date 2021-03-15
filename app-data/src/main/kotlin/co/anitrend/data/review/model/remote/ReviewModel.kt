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

package co.anitrend.data.review.model.remote

import co.anitrend.data.media.model.MediaModel
import co.anitrend.data.review.model.contract.IReviewModel
import co.anitrend.data.user.model.UserModel
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.review.enums.ReviewRating
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** [Review](Notification](https://anilist.github.io/ApiV2-GraphQL-Docs/review.doc.html)
 * A Review that features in an anime or manga
 */
@Serializable
internal sealed class ReviewModel : IReviewModel {

    @Serializable
    internal data class Core(
        @SerialName("body") override val body: String?,
        @SerialName("createdAt") override val createdAt: Long,
        @SerialName("mediaId") override val mediaId: Long,
        @SerialName("mediaType") override val mediaType: MediaType?,
        @SerialName("private") override val private: Boolean?,
        @SerialName("rating") override val rating: Int?,
        @SerialName("ratingAmount") override val ratingAmount: Int?,
        @SerialName("score") override val score: Int?,
        @SerialName("siteUrl") override val siteUrl: String?,
        @SerialName("summary") override val summary: String?,
        @SerialName("updatedAt") override val updatedAt: Long,
        @SerialName("user") override val user: UserModel.Core?,
        @SerialName("userId") override val userId: Long,
        @SerialName("userRating") override val userRating: ReviewRating?,
        @SerialName("id") override val id: Long
    ) : ReviewModel()

    @Serializable
    internal data class Extended(
        @SerialName("media") val media: MediaModel.Core?,
        @SerialName("body") override val body: String?,
        @SerialName("createdAt") override val createdAt: Long,
        @SerialName("mediaId") override val mediaId: Long,
        @SerialName("mediaType") override val mediaType: MediaType?,
        @SerialName("private") override val private: Boolean?,
        @SerialName("rating") override val rating: Int?,
        @SerialName("ratingAmount") override val ratingAmount: Int?,
        @SerialName("score") override val score: Int?,
        @SerialName("siteUrl") override val siteUrl: String?,
        @SerialName("summary") override val summary: String?,
        @SerialName("updatedAt") override val updatedAt: Long,
        @SerialName("user") override val user: UserModel.Core?,
        @SerialName("userId") override val userId: Long,
        @SerialName("userRating") override val userRating: ReviewRating?,
        @SerialName("id") override val id: Long
    ) : ReviewModel()
}