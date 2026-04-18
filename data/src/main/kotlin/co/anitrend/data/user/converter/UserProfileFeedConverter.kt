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

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.arch.data.transformer.ISupportTransformer
import co.anitrend.data.user.entity.sidecar.UserProfileFeedEntity
import co.anitrend.data.user.model.container.UserSidecarModelContainer
import co.anitrend.domain.user.entity.profile.ProfileFeed

internal class UserProfileFeedConverter(
    override val fromType: (UserProfileFeedEntity) -> ProfileFeed = ::transform,
    override val toType: (ProfileFeed) -> UserProfileFeedEntity = { throw NotImplementedError() },
) : SupportConverter<UserProfileFeedEntity, ProfileFeed>() {
    private companion object : ISupportTransformer<UserProfileFeedEntity, ProfileFeed> {
        private val overviewConverter = UserProfileOverviewConverter()

        override fun transform(source: UserProfileFeedEntity) =
            ProfileFeed(
                reviews = source.reviews.map(::reviewPreview),
                listActivity =
                    source.listActivity.map { activity ->
                        overviewConverter.convertFrom(
                            co.anitrend.data.user.entity.sidecar.UserProfileOverviewEntity(
                                id = source.id,
                                recentActivity = listOf(activity),
                            ),
                        ).recentActivity.first()
                    },
            )

        private fun reviewPreview(source: UserSidecarModelContainer.ReviewPreviewPayload) =
            ProfileFeed.ReviewPreview(
                id = source.id,
                summary = source.summary.orEmpty(),
                score = source.score ?: 0,
                rating = source.rating ?: 0,
                ratingAmount = source.ratingAmount ?: 0,
                siteUrl = source.siteUrl.orEmpty(),
                createdAt = source.createdAt,
                updatedAt = source.updatedAt,
                mediaId = source.mediaId,
                mediaType = source.mediaType,
                media = source.media?.let { media ->
                    overviewConverter.convertFrom(
                        co.anitrend.data.user.entity.sidecar.UserProfileOverviewEntity(
                            id = source.mediaId,
                            animeFavourites = listOf(media),
                        ),
                    ).animeFavourites.first()
                },
            )
    }
}
