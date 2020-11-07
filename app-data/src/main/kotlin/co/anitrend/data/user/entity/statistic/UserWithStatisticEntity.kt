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

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import co.anitrend.data.shared.common.Identity
import co.anitrend.data.user.entity.UserEntity

@Entity(
    tableName = "user_statistic",
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
internal data class UserWithStatisticEntity(
    @ColumnInfo(name = "user_id") override val id: Long
) : Identity {
    // This is going to be fun..
}