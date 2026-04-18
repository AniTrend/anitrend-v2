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
package co.anitrend.data.user.entity.sidecar

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import co.anitrend.data.user.entity.UserEntity
import co.anitrend.data.user.model.container.UserSidecarModelContainer

@Entity(
    tableName = "user_profile_feed",
    primaryKeys = ["user_id"],
    indices = [Index(value = ["user_id"])],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
            childColumns = ["user_id"],
            parentColumns = ["id"],
        ),
    ],
)
internal data class UserProfileFeedEntity(
    @ColumnInfo(name = "user_id") val id: Long,
    @ColumnInfo(name = "reviews") val reviews: List<UserSidecarModelContainer.ReviewPreviewPayload> = emptyList(),
    @ColumnInfo(name = "list_activity") val listActivity: List<UserSidecarModelContainer.ListActivityPayload> = emptyList(),
)