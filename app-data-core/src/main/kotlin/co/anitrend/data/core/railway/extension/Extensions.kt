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
package co.anitrend.data.core.railway.extension

import co.anitrend.data.core.railway.OutCome

suspend infix fun <T, U> OutCome<T>.then(spec: suspend (T) -> OutCome<U>) =
    when (this) {
        is OutCome.Pass -> spec(result)
        is OutCome.Fail -> OutCome.Fail(errors)
    }

suspend infix fun <T, U> T.evaluate(spec: suspend (T) -> OutCome<U>) = OutCome.Pass(this) then spec

suspend infix fun <T> OutCome<T>.otherwise(spec: suspend (List<Throwable>) -> Unit) =
    when (this) {
        is OutCome.Fail -> spec(errors)
        else -> Unit
    }
