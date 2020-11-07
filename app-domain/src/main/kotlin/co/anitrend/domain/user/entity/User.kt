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

package co.anitrend.domain.user.entity

import co.anitrend.domain.common.entity.contract.IEntity
import co.anitrend.domain.user.entity.attribute.option.UserMediaListOption
import co.anitrend.domain.user.entity.attribute.option.UserProfileOption
import co.anitrend.domain.user.entity.attribute.statistic.UserMediaStatisticType
import co.anitrend.domain.user.entity.contract.UserImage
import co.anitrend.domain.user.entity.contract.UserStatus

/**
 * User
 */
sealed class User : IEntity {

    abstract val name: CharSequence
    abstract val avatar: UserImage
    abstract val status: UserStatus

    data class Core(
        override val name: CharSequence,
        override val avatar: UserImage,
        override val status: UserStatus,
        override val id: Long
    ) : User()

    data class Extended(
        val unreadNotifications: Int,
        val listOptions: UserMediaListOption,
        val profileOption: UserProfileOption,
        override val name: CharSequence,
        override val avatar: UserImage,
        override val status: UserStatus,
        override val id: Long
    ) : User()

    data class WithStats(
        val unreadNotifications: Int,
        val listOption: UserMediaListOption,
        val profileOption: UserProfileOption,
        val statistics: UserMediaStatisticType,
        override val name: CharSequence,
        override val avatar: UserImage,
        override val status: UserStatus,
        override val id: Long
    ) : User()
}