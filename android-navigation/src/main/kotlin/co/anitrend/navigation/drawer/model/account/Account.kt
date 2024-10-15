/*
 * Copyright (C) 2020 AniTrend
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
        @StringRes val titleRes: Int,
    ) : Account() {
        override val id: Long = titleRes.toLong()
    }

    data class Group(
        @StringRes val titleRes: Int,
        @IdRes val groupId: Int,
    ) : Account() {
        override val id: Long = groupId.toLong()
    }

    /**
     * Indicates whether some other object is "equal to" this one. Implementations must fulfil the following
     * requirements:
     *
     * * Reflexive: for any non-null value `x`, `x.equals(x)` should return true.
     * * Symmetric: for any non-null values `x` and `y`, `x.equals(y)` should return true if and only if `y.equals(x)` returns true.
     * * Transitive:  for any non-null values `x`, `y`, and `z`, if `x.equals(y)` returns true and `y.equals(z)` returns true, then `x.equals(z)` should return true.
     * * Consistent:  for any non-null values `x` and `y`, multiple invocations of `x.equals(y)` consistently return true or consistently return false, provided no information used in `equals` comparisons on the objects is modified.
     * * Never equal to null: for any non-null value `x`, `x.equals(null)` should return false.
     *
     * Read more about [equality](https://kotlinlang.org/docs/reference/equality.html) in Kotlin.
     */
    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Authenticated -> id == other.id
            is Anonymous -> id == other.id
            is Authorize -> id == other.id
            is Group -> id == other.id
            else -> super.equals(other)
        }
    }

    /**
     * Returns a hash code value for the object.  The general contract of `hashCode` is:
     *
     * * Whenever it is invoked on the same object more than once, the `hashCode` method must consistently return the same integer, provided no information used in `equals` comparisons on the object is modified.
     * * If two objects are equal according to the `equals()` method, then calling the `hashCode` method on each of the two objects must produce the same integer result.
     */
    override fun hashCode(): Int {
        return this.hashCode()
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
