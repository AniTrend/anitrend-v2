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
import co.anitrend.domain.common.extension.INVALID_ID
import co.anitrend.domain.user.entity.attribute.MediaListInfo
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

    companion object {
        fun empty() = Core(
            name = "",
            avatar = UserImage.empty(),
            status = UserStatus.empty(),
            id = INVALID_ID,
        )
    }

    data class PreviousName(
        val createdAt: Long?,
        val name: CharSequence?,
        val updatedAt: Long?,
    )

    data class Core(
        override val name: CharSequence,
        override val avatar: UserImage,
        override val status: UserStatus,
        override val id: Long
    ) : User()

    data class Authenticated(
        val unreadNotifications: Int,
        override val name: CharSequence,
        override val avatar: UserImage,
        override val status: UserStatus,
        override val id: Long
    ) : User()

    data class Extended(
        val previousNames: List<PreviousName>,
        val listOption: UserMediaListOption,
        val profileOption: UserProfileOption,
        val mediaListInfo: List<MediaListInfo>,
        override val name: CharSequence,
        override val avatar: UserImage,
        override val status: UserStatus,
        override val id: Long
    ) : User()

    data class WithStats(
        val previousNames: List<PreviousName>,
        val listOption: UserMediaListOption,
        val profileOption: UserProfileOption,
        val statistics: UserMediaStatisticType,
        val mediaListStats: List<MediaListInfo>,
        override val name: CharSequence,
        override val avatar: UserImage,
        override val status: UserStatus,
        override val id: Long
    ) : User()
}