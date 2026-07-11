/*
 * Copyright (C) 2026 AniTrend
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
package co.anitrend.data.android.cache.helper

object CanonicalCacheIdentity {
    fun idFromCanonicalKey(key: String): Long = fnv1a64(key).and(Long.MAX_VALUE)

    private fun fnv1a64(input: String): Long {
        var hash = FNV64_OFFSET_BASIS
        input.forEach { char ->
            hash = hash xor char.code.toLong()
            hash *= FNV64_PRIME
        }
        return hash
    }

    private const val FNV64_OFFSET_BASIS = -3750763034362895579L
    private const val FNV64_PRIME = 1099511628211L
}
