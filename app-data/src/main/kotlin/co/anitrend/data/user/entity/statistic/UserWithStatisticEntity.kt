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

package co.anitrend.data.user.entity.statistic

import androidx.room.*
import co.anitrend.data.shared.common.Identity
import co.anitrend.data.user.entity.UserEntity
import co.anitrend.data.user.model.statistics.UserStatisticModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(
    tableName = "user_statistic",
    indices = [Index(value = ["user_id"])],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
            childColumns = ["user_id"],
            parentColumns = ["id"],
            deferred = true
        )
    ]
)
internal data class UserWithStatisticEntity(
    @Embedded(prefix = "statistic_") val statistic: Statistic,
    @ColumnInfo(name = "user_id") val userId: Long,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") override val id: Long = 0
) : Identity {
    @Serializable
    data class Statistic(
        @SerialName("anime") @ColumnInfo(name = "anime") val anime: UserStatisticModel.Anime?,
        @SerialName("manga") @ColumnInfo(name = "manga") val manga: UserStatisticModel.Manga?
    )
}