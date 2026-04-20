/*
 * Copyright (C) 2026 AniTrend
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
package co.anitrend.domain.user.entity.profile

import co.anitrend.domain.media.enums.MediaType

data class ProfileFeed(
    val reviews: List<ReviewPreview>,
    val listActivity: List<ProfileOverview.ListActivityPreview>,
) {
    data class ReviewPreview(
        val id: Long,
        val summary: CharSequence,
        val score: Int,
        val rating: Int,
        val ratingAmount: Int,
        val siteUrl: String,
        val createdAt: Long,
        val updatedAt: Long,
        val mediaId: Long,
        val mediaType: MediaType?,
        val media: ProfileOverview.MediaPreview?,
    )
}
