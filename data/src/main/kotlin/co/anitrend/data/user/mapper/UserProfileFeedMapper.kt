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

import co.anitrend.data.android.database.common.TransactionRunner
import co.anitrend.data.android.mapper.EmbedMapper
import co.anitrend.data.android.mapper.DefaultMapper
import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.data.media.model.MediaModel
import co.anitrend.data.review.mapper.ReviewMapper
import co.anitrend.data.status.mapper.StatusMapper
import co.anitrend.data.user.model.container.UserSidecarModelContainer

internal class UserProfileFeedMapper(
    private val reviewConnectionMapper: UserProfileConnectionMapper.ReviewEmbed,
    private val reviewPreviewMapper: ReviewMapper.PreviewEmbed,
    private val statusEmbedMapper: StatusMapper.Activity.Embed,
    private val mediaEmbedMapper: EmbedMapper<MediaModel, MediaEntity>,
    private val writer: UserProfileFeedWriterContract,
    private val transactionRunner: TransactionRunner,
) : DefaultMapper<UserSidecarModelContainer.Feed, Unit>() {
    override suspend fun persist(data: Unit) {
        writer.persist()
    }

    override suspend fun onResponseDatabaseInsert(mappedData: Unit) {
        transactionRunner.run {
            super.onResponseDatabaseInsert(mappedData)
        }
    }

    override suspend fun onResponseMapFrom(source: UserSidecarModelContainer.Feed) {
        val userId = requireNotNull(source.user?.id) { "Feed response missing user id" }

        val reviewMedia =
            source.reviewPage
                ?.reviews
                .orEmpty()
                .mapNotNull { it.media }
        val activityMedia =
            source.activityPage
                ?.listActivity
                .orEmpty()
                .mapNotNull { it.media }
        mediaEmbedMapper.onEmbedded(reviewMedia + activityMedia)

        reviewPreviewMapper.onEmbedded(
            source.reviewPage?.reviews.orEmpty().map { review ->
                ReviewMapper.PreviewEmbed.Item(
                    userId = userId,
                    preview = review,
                )
            },
        )

        reviewConnectionMapper.onEmbedded(
            source.reviewPage?.reviews.orEmpty().mapIndexed { index, review ->
                UserProfileConnectionMapper.ReviewEmbed.Item(
                    userId = userId,
                    reviewId = review.id,
                    sortIndex = index,
                    mediaId = review.mediaId,
                )
            },
        )

        statusEmbedMapper.onEmbedded(
            StatusMapper.Activity.Embed.asItems(
                userId = userId,
                source = source.activityPage?.listActivity.orEmpty(),
            ),
        )
    }
}
