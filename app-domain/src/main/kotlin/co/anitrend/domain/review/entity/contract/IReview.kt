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

package co.anitrend.domain.review.entity.contract

import co.anitrend.domain.common.entity.contract.IEntity
import co.anitrend.domain.common.entity.contract.ISynopsis
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.review.enums.ReviewRating
import co.anitrend.domain.user.entity.User

interface IReview : IEntity, ISynopsis {
    val createdAt: Long
    val mediaId: Long
    val mediaType: MediaType
    val private: Boolean
    val rating: Int
    val ratingAmount: Int
    val score: Int
    val siteUrl: String
    val summary: String
    val updatedAt: Long
    val user: User
    val userId: Long
    val userRating: ReviewRating
}