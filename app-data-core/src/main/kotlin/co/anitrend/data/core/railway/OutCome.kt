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

package co.anitrend.data.core.railway

/**
 * Representation of the result of an expression
 */
sealed class OutCome<T> {
    /**
     * Represent the success of an expression
     */
    data class Pass<T>(
        val result: T
    ) : OutCome<T>()

    /**
     * Represent the failure of an expression
     */
    data class Fail<T>(
        val errors: List<Throwable>
    ) : OutCome<T>()
}