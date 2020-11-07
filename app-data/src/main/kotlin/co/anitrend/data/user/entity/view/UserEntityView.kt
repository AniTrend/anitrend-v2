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

package co.anitrend.data.user.entity.view

import androidx.room.Embedded
import androidx.room.Relation
import co.anitrend.data.user.entity.UserEntity
import co.anitrend.data.user.entity.option.UserGeneralOptionEntity
import co.anitrend.data.user.entity.option.UserMediaOptionEntity
import co.anitrend.data.user.entity.statistic.UserWithStatisticEntity

internal sealed class UserEntityView {
    abstract val user: UserEntity

    internal data class WithOptions(
        @Embedded override val user: UserEntity,
        @Relation(
            parentColumn = "id",
            entityColumn = "user_id"
        )
        val generalOption: UserGeneralOptionEntity,
        @Relation(
            parentColumn = "id",
            entityColumn = "user_id"
        )
        val mediaListOption: UserMediaOptionEntity
    ) : UserEntityView()

    internal data class WithStatistic(
        @Embedded override val user: UserEntity,
        @Relation(
            parentColumn = "id",
            entityColumn = "user_id"
        )
        val statistic: UserWithStatisticEntity
    ) : UserEntityView()
}