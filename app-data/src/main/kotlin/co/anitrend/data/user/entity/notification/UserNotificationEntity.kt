/*
 * Copyright (C) 2022  AniTrend
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

package co.anitrend.data.user.entity.notification

import androidx.room.*
import co.anitrend.data.core.common.Identity
import co.anitrend.data.user.entity.UserEntity
import co.anitrend.support.query.builder.annotation.EntitySchema

@Entity(
    tableName = "user_notification",
    indices = [
        Index(
            value = ["user_id"],
            unique = true
        )
    ],
    foreignKeys = [
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
internal data class UserNotificationEntity(
    @ColumnInfo(name = "user_id") val userId: Long,
    @ColumnInfo(name = "unread_notifications") val unreadNotifications: Int,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") override val id: Long = 0
) : Identity