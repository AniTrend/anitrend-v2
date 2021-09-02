/*
 * Copyright (C) 2020  AniTrend
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

package co.anitrend.data.user.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import co.anitrend.data.core.common.Identity
import co.anitrend.support.query.builder.annotation.EntitySchema

@Entity(
    tableName = "user",
    primaryKeys = ["id"],
    indices = [
        Index(value = ["user_name"], unique = true),
    ],
)
@EntitySchema
internal data class UserEntity(
    @Embedded(prefix = "user_") val about: About,
    @Embedded(prefix = "user_") val status: Status?,
    @Embedded(prefix = "cover_") val coverImage: CoverImage,
    @ColumnInfo(name = "unread_notifications") val unreadNotification: Int?,
    @ColumnInfo(name = "updated_at") val updatedAt: Long?,
    @ColumnInfo(name = "created_at") val createdAt: Long?,
    @ColumnInfo(name = "id") override val id: Long
) : Identity {

    data class About(
        @ColumnInfo(name = "name") val name: String,
        @ColumnInfo(name = "bio") val bio: String?,
        @ColumnInfo(name = "site_url") val siteUrl: String,
        @ColumnInfo(name = "donator_tier") val donatorTier: Int?,
        @ColumnInfo(name = "donator_badge") val donatorBadge: String?,
    )

    data class Status(
        @ColumnInfo(name = "is_following") val isFollowing: Boolean? = null,
        @ColumnInfo(name = "is_follower") val isFollower: Boolean? = null,
        @ColumnInfo(name = "is_blocked") val isBlocked: Boolean = false,
    )

    data class CoverImage(
        @ColumnInfo(name = "large") val large: String? = null,
        @ColumnInfo(name = "medium") val medium: String? = null,
        @ColumnInfo(name = "banner") val banner: String? = null
    )
}