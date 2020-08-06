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

package co.anitrend.data.arch.railway.extension

import co.anitrend.data.arch.railway.OutCome

infix fun <T,U> OutCome<T>.then(
    spec: (T) -> OutCome<U>
) = when (this) {
    is OutCome.Pass -> spec(result)
    is OutCome.Fail -> OutCome.Fail(errors)
}

infix fun <T,U> T.to(
    spec: (T) -> OutCome<U>
) = OutCome.Pass(this) then spec

infix fun <T> OutCome<T>.otherwise(
    spec: (List<Throwable>) -> Unit
) = when (this) {
    is OutCome.Fail -> spec(errors)
    else -> Unit
}