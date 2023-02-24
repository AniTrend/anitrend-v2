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

package co.anitrend.data.review.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import co.anitrend.data.core.common.Identity
import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.data.user.entity.UserEntity
import co.anitrend.domain.review.enums.ReviewRating
import co.anitrend.support.query.builder.annotation.EntitySchema

@Entity(
    tableName = "review",
    primaryKeys = ["id"],
    indices = [
        Index(value = ["media_id", "user_id"]),
        Index(value = ["user_id"]),
        Index(value = ["media_id"]),
    ],
    foreignKeys = [
        ForeignKey(
            entity = MediaEntity::class,
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
            childColumns = ["media_id"],
            parentColumns = ["id"]
        ),
        ForeignKey(
            entity = UserEntity::class,
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
            childColumns = ["user_id"],
            parentColumns = ["id"]
        )
    ]
)
@EntitySchema
internal data class ReviewEntity(
    @ColumnInfo(name = "body") val body: String,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "media_id") val mediaId: Long,
    @ColumnInfo(name = "is_private") val isPrivate: Boolean,
    @ColumnInfo(name = "rating") val rating: Int,
    @ColumnInfo(name = "rating_amount") val ratingAmount: Int,
    @ColumnInfo(name = "score") val score: Int,
    @ColumnInfo(name = "site_url") val siteUrl: String,
    @ColumnInfo(name = "summary") val summary: String,
    @ColumnInfo(name = "updated_at") val updatedAt: Long,
    @ColumnInfo(name = "user_id") val userId: Long,
    @ColumnInfo(name = "user_rating") val userRating: ReviewRating,
    @ColumnInfo(name = "id") override val id: Long
) : Identity