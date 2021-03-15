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

package co.anitrend.navigation.drawer.model.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes

sealed class Navigation {
    abstract val id: Int

    data class Menu(
        override val id: Int,
        @DrawableRes val icon: Int,
        @StringRes val titleRes: Int,
        val isCheckable: Boolean = true,
        var isChecked: Boolean = false
    ) : Navigation() {

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
                is Menu -> other.id == id &&
                        other.titleRes == titleRes &&
                        other.isChecked == isChecked &&
                        other.isCheckable == isCheckable
                else -> super.equals(other)
            }
        }

        override fun hashCode(): Int {
            var result = id
            result = 31 * result + icon
            result = 31 * result + titleRes
            result = 31 * result + isCheckable.hashCode()
            result = 31 * result + isChecked.hashCode()
            return result
        }
    }

    data class Divider(
        override val id: Int = DIVIDER
    ) : Navigation() {

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
                is Divider -> other.id == id
                else -> super.equals(other)
            }
        }

        override fun hashCode(): Int {
            return id
        }
    }

    data class Group(
        @StringRes val titleRes: Int,
        @IdRes val groupId: Int
    ) : Navigation() {
        override val id: Int = groupId

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
                is Group -> other.groupId == groupId && other.titleRes == titleRes
                else -> super.equals(other)
            }
        }

        override fun hashCode(): Int {
            var result = super.hashCode()
            result = 31 * result + titleRes
            result = 31 * result + groupId
            result = 31 * result + id
            return result
        }
    }

    companion object {

        internal const val MENU = 1
        internal const val DIVIDER = 2
        internal const val GROUP = 3

        internal fun Navigation?.toNavType(): Int? {
            return when (this) {
                is Menu -> MENU
                is Divider -> DIVIDER
                is Group -> GROUP
                else -> null
            }
        }
    }
}