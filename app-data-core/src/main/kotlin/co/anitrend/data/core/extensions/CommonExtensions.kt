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

package co.anitrend.data.core.extensions

import com.google.gson.reflect.TypeToken
import timber.log.Timber

/**
 * Hash code generator with the ability to mask negative values
 */
fun String.toHashId(): Long {
    val hash = hashCode()
    if (hash < 0) {
        val id = (hash and 0x7fffffff).toLong()
        Timber.v("Hash id generated -> $id")
        return id
    }
    Timber.v("Hash id default -> $hash")
    return hash.toLong()
}

/**
 * Type token resolver helper
 *
 * @return [TypeToken]
 */
inline fun <reified T> typeToken(): TypeToken<T> =
    object : TypeToken<T>(){}