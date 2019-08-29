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

package co.anitrend.data.model.response.core.review

import co.anitrend.data.model.response.core.media.contract.IMedia
import co.anitrend.data.model.response.core.review.contract.IReview
import co.anitrend.data.model.response.core.user.contract.IUser
import co.anitrend.data.usecase.media.attributes.MediaType
import co.anitrend.data.usecase.review.attributes.ReviewRating

/** [Review](Notification](https://anilist.github.io/ApiV2-GraphQL-Docs/review.doc.html)
 * A Review that features in an anime or manga
 */
class Review(
    override val body: String?,
    override val createdAt: Long,
    override val media: IMedia?,
    override val mediaId: Long,
    override val mediaType: MediaType?,
    override val private: Boolean?,
    override val rating: Int?,
    override val ratingAmount: Int?,
    override val score: Int?,
    override val siteUrl: String?,
    override val summary: String?,
    override val updatedAt: Long,
    override val user: IUser?,
    override val userId: Long,
    override val userRating: ReviewRating?,
    override val id: Long
) : IReview