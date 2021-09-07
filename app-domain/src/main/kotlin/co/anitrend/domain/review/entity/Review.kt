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

package co.anitrend.domain.review.entity

import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.review.entity.contract.IReview
import co.anitrend.domain.review.enums.ReviewRating
import co.anitrend.domain.user.entity.User

sealed class Review : IReview {

    data class Core(
        override val body: String,
        override val createdAt: Long,
        override val mediaId: Long,
        override val mediaType: MediaType,
        override val private: Boolean,
        override val rating: Int,
        override val ratingAmount: Int,
        override val score: Int,
        override val siteUrl: String,
        override val summary: String,
        override val updatedAt: Long,
        override val user: User,
        override val userId: Long,
        override val userRating: ReviewRating,
        override val id: Long
    ) : Review()

    data class Extended(
        val media: Media,
        override val body: String,
        override val createdAt: Long,
        override val mediaId: Long,
        override val mediaType: MediaType,
        override val private: Boolean,
        override val rating: Int,
        override val ratingAmount: Int,
        override val score: Int,
        override val siteUrl: String,
        override val summary: String,
        override val updatedAt: Long,
        override val user: User,
        override val userId: Long,
        override val userRating: ReviewRating,
        override val id: Long
    ) : Review()
}
