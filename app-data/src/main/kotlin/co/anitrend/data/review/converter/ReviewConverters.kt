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

package co.anitrend.data.review.converter

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.arch.data.transformer.ISupportTransformer
import co.anitrend.data.media.converter.MediaConverter
import co.anitrend.data.media.converter.MediaEntityViewConverter
import co.anitrend.data.media.converter.MediaModelConverter
import co.anitrend.data.media.model.MediaModel
import co.anitrend.data.review.entity.ReviewEntity
import co.anitrend.data.review.entity.view.ReviewEntityView
import co.anitrend.data.review.model.remote.ReviewModel
import co.anitrend.data.user.converter.UserEntityConverter
import co.anitrend.domain.medialist.entity.MediaList
import co.anitrend.domain.review.entity.Review
import co.anitrend.domain.review.enums.ReviewRating

internal class ReviewModelConverter(
    override val fromType: (ReviewModel) -> ReviewEntity = ::transform,
    override val toType: (ReviewEntity) -> ReviewModel = { throw NotImplementedError() }
) : SupportConverter<ReviewModel, ReviewEntity>() {
    private companion object : ISupportTransformer<ReviewModel, ReviewEntity> {
        override fun transform(source: ReviewModel): ReviewEntity =
            ReviewEntity(
                body = source.body.orEmpty(),
                createdAt = source.createdAt,
                mediaId = source.mediaId,
                isPrivate = source.private ?: false,
                rating = source.rating ?: 0,
                ratingAmount = source.ratingAmount ?: 0,
                score = source.score ?: 0,
                siteUrl = source.siteUrl.orEmpty(),
                summary = source.summary.orEmpty(),
                updatedAt = source.updatedAt,
                userId = source.userId,
                userRating = source.userRating ?: ReviewRating.NO_VOTE,
                id = source.id,
            )
    }
}

internal class ReviewEntityViewConverter(
    override val fromType: (ReviewEntityView) -> Review = ::transform,
    override val toType: (Review) -> ReviewEntityView = { throw NotImplementedError() }
) : SupportConverter<ReviewEntityView, Review>() {
    private companion object : ISupportTransformer<ReviewEntityView, Review> {
        override fun transform(source: ReviewEntityView): Review =
            Review.Extended(
                media = MediaEntityViewConverter().convertFrom(source.media),
                description = source.review.body,
                createdAt = source.review.createdAt,
                mediaId = source.review.mediaId,
                mediaType = source.media.media.type,
                private = source.review.isPrivate,
                rating = source.review.rating,
                ratingAmount = source.review.ratingAmount,
                score = source.review.score,
                siteUrl = source.review.siteUrl,
                summary = source.review.summary,
                updatedAt = source.review.updatedAt,
                user = UserEntityConverter().convertFrom(source.user),
                userId = source.review.userId,
                userRating = source.review.userRating,
                id = source.review.id,
            )
    }
}