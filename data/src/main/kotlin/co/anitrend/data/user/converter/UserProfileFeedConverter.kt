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

import co.anitrend.data.status.entity.StatusEntity
import co.anitrend.data.user.entity.connection.UserProfileReviewEntity
import co.anitrend.domain.media.entity.attribute.image.MediaImage
import co.anitrend.domain.media.entity.attribute.title.MediaTitle
import co.anitrend.domain.user.entity.profile.ProfileFeed
import co.anitrend.domain.user.entity.profile.ProfileOverview

internal object UserProfileFeedConverter {

    fun toProfileFeed(
        reviews: List<UserProfileReviewEntity>,
        activities: List<StatusEntity.ListStatus>,
    ): ProfileFeed =
        ProfileFeed(
            reviews = reviews.map { reviewPreview(it) },
            listActivity = activities.map { UserProfileOverviewConverter.listActivityPreview(it) },
        )

    private fun reviewMediaPreview(source: UserProfileReviewEntity): ProfileOverview.MediaPreview? {
        val mediaId = source.mediaId.takeIf { it != 0L } ?: return null
        return ProfileOverview.MediaPreview(
            id = mediaId,
            title =
                MediaTitle(
                    romaji = source.mediaTitleRomaji,
                    english = source.mediaTitleEnglish,
                    native = source.mediaTitleNative,
                    userPreferred = source.mediaTitleUserPreferred,
                ),
            image =
                MediaImage(
                    color = source.mediaCoverColor,
                    extraLarge = null,
                    large = source.mediaCoverLarge,
                    medium = source.mediaCoverMedium,
                    banner = null,
                ),
            type = source.mediaEntityType,
            format = source.mediaFormat,
            status = source.mediaStatus,
            episodes = source.mediaEpisodes ?: 0,
            chapters = source.mediaChapters ?: 0,
            volumes = source.mediaVolumes ?: 0,
            isFavourite = source.mediaIsFavourite ?: false,
            meanScore = source.mediaMeanScore ?: 0,
            averageScore = source.mediaAverageScore ?: 0,
            siteUrl = source.mediaSiteUrl,
        )
    }

    private fun reviewPreview(source: UserProfileReviewEntity): ProfileFeed.ReviewPreview =
        ProfileFeed.ReviewPreview(
            id = source.reviewId,
            summary = source.summary.orEmpty(),
            score = source.score ?: 0,
            rating = source.rating ?: 0,
            ratingAmount = source.ratingAmount ?: 0,
            siteUrl = source.siteUrl.orEmpty(),
            createdAt = source.createdAt,
            updatedAt = source.updatedAt,
            mediaId = source.mediaId,
            mediaType = source.mediaType,
            media = reviewMediaPreview(source),
        )
}
