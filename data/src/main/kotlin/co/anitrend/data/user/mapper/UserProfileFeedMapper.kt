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
package co.anitrend.data.user.mapper

import co.anitrend.data.android.mapper.DefaultMapper
import co.anitrend.data.status.datasource.local.StatusLocalSource
import co.anitrend.data.status.entity.StatusEntity
import co.anitrend.data.user.datasource.local.connection.UserProfileReviewLocalSource
import co.anitrend.data.user.entity.connection.UserProfileReviewEntity
import co.anitrend.data.user.mapper.UserProfileOverviewMapper.Companion.toListStatusEntity
import co.anitrend.data.user.model.container.UserSidecarModelContainer

internal class UserProfileFeedMapper(
    private val reviewLocalSource: UserProfileReviewLocalSource,
    private val statusLocalSource: StatusLocalSource,
) : DefaultMapper<UserSidecarModelContainer.Feed, Unit>() {

    private var pendingReviews: List<UserProfileReviewEntity> = emptyList()
    private var pendingActivities: List<StatusEntity.ListStatus> = emptyList()

    override suspend fun persist(data: Unit) {
        reviewLocalSource.upsert(pendingReviews)
        statusLocalSource.upsert(pendingActivities)
    }

    override suspend fun onResponseMapFrom(source: UserSidecarModelContainer.Feed): Unit {
        val userId = requireNotNull(source.user?.id) { "Feed response missing user id" }

        pendingReviews = source.reviewPage?.reviews.orEmpty().mapIndexed { index, review ->
            UserProfileReviewEntity(
                userId = userId,
                reviewId = review.id,
                sortIndex = index,
                summary = review.summary,
                score = review.score,
                rating = review.rating,
                ratingAmount = review.ratingAmount,
                siteUrl = review.siteUrl,
                createdAt = review.createdAt,
                updatedAt = review.updatedAt,
                mediaId = review.mediaId,
                mediaType = review.mediaType,
                mediaTitleRomaji = review.media?.title?.romaji,
                mediaTitleEnglish = review.media?.title?.english,
                mediaTitleNative = review.media?.title?.nativeTitle,
                mediaTitleUserPreferred = review.media?.title?.userPreferred,
                mediaCoverColor = review.media?.image?.color,
                mediaCoverLarge = review.media?.image?.large,
                mediaCoverMedium = review.media?.image?.medium,
                mediaEntityType = review.media?.type,
                mediaFormat = review.media?.format,
                mediaStatus = review.media?.status,
                mediaEpisodes = review.media?.episodes,
                mediaChapters = review.media?.chapters,
                mediaVolumes = review.media?.volumes,
                mediaIsFavourite = review.media?.isFavourite,
                mediaMeanScore = review.media?.meanScore,
                mediaAverageScore = review.media?.averageScore,
                mediaSiteUrl = review.media?.siteUrl,
                mediaListStatus = review.media?.mediaList?.status,
                mediaListProgress = review.media?.mediaList?.progress,
                mediaListVolumeProgress = review.media?.mediaList?.progressVolumes,
            )
        }

        pendingActivities = source.activityPage?.listActivity.orEmpty().mapIndexed { index, activity ->
            activity.toListStatusEntity(userId, index)
        }
    }
}

