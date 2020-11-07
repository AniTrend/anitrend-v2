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

package co.anitrend.data.user.entity.option

import androidx.room.*
import co.anitrend.data.shared.common.Identity
import co.anitrend.data.user.entity.UserEntity
import co.anitrend.domain.notification.enums.NotificationType
import co.anitrend.domain.user.enums.UserTitleLanguage
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(
    tableName = "user_general_option",
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
internal data class UserGeneralOptionEntity(
    @ColumnInfo(name = "user_id") val userId: Long,
    @ColumnInfo(name = "airing_notifications") val airingNotifications: Boolean,
    @ColumnInfo(name = "display_adult_content") val displayAdultContent: Boolean,
    @ColumnInfo(name = "notification_option") val notificationOption: List<NotificationOption>,
    @ColumnInfo(name = "title_language") val titleLanguage: UserTitleLanguage,
    @ColumnInfo(name = "profile_color") val profileColor: String?,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") override val id: Long = 0
) : Identity {

    @Serializable
    data class NotificationOption(
        @SerialName("enabled") val enabled: Boolean,
        @SerialName("notification_type") val notificationType: NotificationType
    )
}