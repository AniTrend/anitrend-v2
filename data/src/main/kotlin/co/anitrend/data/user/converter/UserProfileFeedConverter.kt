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
package co.anitrend.data.user.converter

import co.anitrend.data.status.entity.view.ListStatusEntityView
import co.anitrend.data.user.entity.view.UserProfileReviewEntityView
import co.anitrend.domain.user.entity.profile.ProfileFeed

internal object UserProfileFeedConverter {
    fun toProfileFeed(
        reviews: List<UserProfileReviewEntityView>,
        activities: List<ListStatusEntityView>,
    ): ProfileFeed =
        ProfileFeed(
            reviews = reviews.map { reviewPreview(it) },
            listActivity = activities.map { UserProfileOverviewConverter.listActivityPreview(it) },
        )

    private fun reviewPreview(source: UserProfileReviewEntityView): ProfileFeed.ReviewPreview =
        ProfileFeed.ReviewPreview(
            id = source.review.id,
            summary = source.review.summary,
            score = source.review.score,
            rating = source.review.rating,
            ratingAmount = source.review.ratingAmount,
            siteUrl = source.review.siteUrl,
            createdAt = source.review.createdAt,
            updatedAt = source.review.updatedAt,
            mediaId = source.review.mediaId,
            mediaType = source.media.type,
            media = UserProfileOverviewConverter.mediaPreview(source.media),
        )
}
