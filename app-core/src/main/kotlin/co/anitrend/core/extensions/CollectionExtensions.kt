/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.core.extensions

inline fun <P, S> List<P>.union(
    secondary: List<S>,
    predicate: (P, S) -> Boolean
) = filter { p ->
    secondary.any { s ->
        predicate(p, s)
    }
}

fun <T> List<T>?.combine(item: T?): List<T> {
    return if (item != null)
        orEmpty().plus(listOf(item))
    else orEmpty()
}