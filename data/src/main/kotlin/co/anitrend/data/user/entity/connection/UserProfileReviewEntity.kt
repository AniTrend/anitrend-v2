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
package co.anitrend.data.user.entity.connection

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.data.review.entity.ReviewEntity
import co.anitrend.data.user.entity.UserEntity
import co.anitrend.support.query.builder.annotation.EntitySchema

/**
 * User-scoped review preview cache. Stores the subset of review fields available from the
 * profile-feed API query (summary, score, ratings, dates, media preview). The full review
 * body, privacy flag, and user-vote rating are not available at this query level, so this
 * entity is intentionally distinct from [co.anitrend.data.review.entity.ReviewEntity].
 *
 * Inline media preview columns follow the flat-column pattern used by
 * [co.anitrend.data.media.entity.connection.MediaStaffConnectionEntity] to avoid a runtime
 * join and keep this entity self-contained.
 *
 * Table: `user_profile_review`
 */
@Entity(
    tableName = "user_profile_review",
    indices = [
        Index(value = ["user_id", "review_id", "media_id"], unique = true),
        Index(value = ["user_id", "sort_index"]),
        Index(value = ["review_id"]),
        Index(value = ["media_id"]),
    ],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = ReviewEntity::class,
            parentColumns = ["id"],
            childColumns = ["review_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = MediaEntity::class,
            parentColumns = ["id"],
            childColumns = ["media_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
    ],
)
@EntitySchema
internal data class UserProfileReviewEntity(
    @ColumnInfo(name = "user_id") val userId: Long,
    @ColumnInfo(name = "review_id") val reviewId: Long,
    @ColumnInfo(name = "sort_index") val sortIndex: Int,
    @ColumnInfo(name = "media_id") val mediaId: Long,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long = 0,
)
