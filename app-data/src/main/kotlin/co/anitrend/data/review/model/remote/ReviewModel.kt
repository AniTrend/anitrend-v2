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

import co.anitrend.data.media.model.MediaModelCore
import co.anitrend.data.review.model.contract.IReviewModel
import co.anitrend.data.user.model.remote.UserModelCore
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.review.enums.ReviewRating
import com.google.gson.annotations.SerializedName

/** [Review](Notification](https://anilist.github.io/ApiV2-GraphQL-Docs/review.doc.html)
 * A Review that features in an anime or manga
 */
internal class ReviewModel(
    @SerializedName("body") override val body: String?,
    @SerializedName("createdAt") override val createdAt: Long,
    @SerializedName("media") override val media: MediaModelCore?,
    @SerializedName("mediaId") override val mediaId: Long,
    @SerializedName("mediaType") override val mediaType: MediaType?,
    @SerializedName("private") override val private: Boolean?,
    @SerializedName("rating") override val rating: Int?,
    @SerializedName("ratingAmount") override val ratingAmount: Int?,
    @SerializedName("score") override val score: Int?,
    @SerializedName("siteUrl") override val siteUrl: String?,
    @SerializedName("summary") override val summary: String?,
    @SerializedName("updatedAt") override val updatedAt: Long,
    @SerializedName("user") override val user: UserModelCore?,
    @SerializedName("userId") override val userId: Long,
    @SerializedName("userRating") override val userRating: ReviewRating?,
    @SerializedName("id") override val id: Long
) : IReviewModel