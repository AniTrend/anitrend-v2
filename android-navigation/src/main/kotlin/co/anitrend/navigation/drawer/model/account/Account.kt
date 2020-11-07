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

package co.anitrend.navigation.drawer.model.account

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import co.anitrend.domain.common.entity.contract.ICoverImage
import co.anitrend.navigation.drawer.model.navigation.Navigation

sealed class Account {
    abstract val id: Long
    open val isActiveUser: Boolean = false

    data class Authenticated(
        override val id: Long,
        override val isActiveUser: Boolean,
        val userName: CharSequence,
        val coverImage: ICoverImage,
    ) : Account()

    data class Anonymous(
        @StringRes val titleRes: Int,
        @DrawableRes val imageRes: Int,
        override val isActiveUser: Boolean,
    ) : Account() {
        override val id: Long = titleRes.toLong()
    }

    data class Authorize(
        @StringRes val titleRes: Int
    ) : Account() {
        override val id: Long = titleRes.toLong()
    }

    data class Group(
        @StringRes val titleRes: Int,
        @IdRes val groupId: Int
    ) : Account() {
        override val id: Long = groupId.toLong()
    }

    companion object {

        internal const val AUTHENTICATED = 1
        internal const val AUTHORIZE = 2
        internal const val ANONYMOUS = 3
        internal const val GROUP = 4

        internal fun Account?.toAccountType(): Int? {
            return when (this) {
                is Authenticated -> AUTHENTICATED
                is Anonymous -> ANONYMOUS
                is Authorize -> AUTHORIZE
                is Group -> GROUP
                else -> null
            }
        }
    }
}