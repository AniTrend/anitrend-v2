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

package co.anitrend.data.review.entity.view

import androidx.room.Embedded
import androidx.room.Relation
import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.data.media.entity.view.MediaEntityView
import co.anitrend.data.review.entity.ReviewEntity
import co.anitrend.data.user.entity.UserEntity

internal sealed class ReviewEntityView {
    abstract val review: ReviewEntity
    abstract val user: UserEntity
    abstract val media: MediaEntityView.Core

    data class Core(
        @Embedded override val review: ReviewEntity,
        @Relation(
            parentColumn = "user_id",
            entityColumn = "id"
        )
        override val user: UserEntity,
        @Relation(
            parentColumn = "media_id",
            entityColumn = "id",
            entity = MediaEntity::class
        )
        override val media: MediaEntityView.Core,
    ) : ReviewEntityView()
}