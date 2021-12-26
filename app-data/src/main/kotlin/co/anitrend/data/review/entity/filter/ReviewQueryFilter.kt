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

package co.anitrend.data.review.entity.filter

import co.anitrend.data.android.filter.FilterQueryBuilder
import co.anitrend.data.media.entity.MediaEntitySchema
import co.anitrend.data.review.entity.ReviewEntitySchema
import co.anitrend.data.user.entity.UserEntitySchema
import co.anitrend.domain.review.enums.ReviewSort
import co.anitrend.domain.review.model.ReviewParam
import co.anitrend.support.query.builder.core.criteria.extensions.equal
import co.anitrend.support.query.builder.core.from.extentions.asTable
import co.anitrend.support.query.builder.core.from.extentions.innerJoin
import co.anitrend.support.query.builder.core.projection.extensions.asColumn
import co.anitrend.support.query.builder.dsl.from
import co.anitrend.support.query.builder.dsl.whereAnd

internal sealed class ReviewQueryFilter<T> : FilterQueryBuilder<T>() {

    protected val mediaTable = MediaEntitySchema.tableName.asTable()
    protected val userTable = UserEntitySchema.tableName.asTable()
    protected val reviewTable = ReviewEntitySchema.tableName.asTable()

    /**
     * Staring point of the query builder, that should make use of [requireBuilder]
     * to add query objections
     */
    override fun onBuildQuery(filter: T) {
        requireBuilder() from (reviewTable).innerJoin(mediaTable).on(
            MediaEntitySchema.id.asColumn(mediaTable).equal(
                ReviewEntitySchema.mediaId.asColumn(reviewTable)
            )
        ).innerJoin(userTable).on(
            UserEntitySchema.id.asColumn(userTable).equal(
                ReviewEntitySchema.userId.asColumn(reviewTable)
            )
        )
    }

    class Paged : ReviewQueryFilter<ReviewParam.Paged>() {

        private fun selection(filter: ReviewParam.Paged) {
            filter.mediaType?.also { mediaType ->
                requireBuilder() whereAnd {
                    MediaEntitySchema.type.asColumn(mediaTable).equal(
                        mediaType.toString()
                    )
                }
            }
            filter.mediaId?.also { mediaId ->
                requireBuilder() whereAnd {
                    MediaEntitySchema.id.asColumn(mediaTable).equal(
                        mediaId
                    )
                }
            }
            filter.userId?.also { userId ->
                requireBuilder() whereAnd {
                    UserEntitySchema.id.asColumn(userTable).equal(
                        userId
                    )
                }
            }
        }


        private fun order(filter: ReviewParam.Paged) {
            filter.sort?.forEach { sort ->
                when (sort.sortable) {
                    ReviewSort.CREATED_AT -> requireBuilder().orderBy(
                        ReviewEntitySchema.createdAt.asColumn(reviewTable), sort.order
                    )
                    ReviewSort.ID -> requireBuilder().orderBy(
                        ReviewEntitySchema.id.asColumn(reviewTable), sort.order
                    )
                    ReviewSort.RATING -> requireBuilder().orderBy(
                        ReviewEntitySchema.rating.asColumn(reviewTable), sort.order
                    )
                    ReviewSort.SCORE -> requireBuilder().orderBy(
                        ReviewEntitySchema.score.asColumn(reviewTable), sort.order
                    )
                    ReviewSort.UPDATED_AT -> requireBuilder().orderBy(
                        ReviewEntitySchema.updatedAt.asColumn(reviewTable), sort.order
                    )
                }
            }
        }

        /**
         * Staring point of the query builder, that should make use of [requireBuilder]
         * to add query objections
         */
        override fun onBuildQuery(filter: ReviewParam.Paged) {
            super.onBuildQuery(filter)
            selection(filter)
            order(filter)
        }
    }

    class Entry : ReviewQueryFilter<ReviewParam.Entry>() {

        private fun selection(filter: ReviewParam.Entry) {
            filter.id.also { id ->
                requireBuilder() whereAnd {
                    ReviewEntitySchema.id.asColumn(reviewTable).equal(
                        id
                    )
                }
            }
        }

        /**
         * Staring point of the query builder, that should make use of [requireBuilder]
         * to add query objections
         */
        override fun onBuildQuery(filter: ReviewParam.Entry) {
            super.onBuildQuery(filter)
            selection(filter)
        }
    }
}