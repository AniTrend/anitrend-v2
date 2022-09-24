/*
 * Copyright (C) 2022  AniTrend
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

package co.anitrend.buildSrc.common

object Configuration {

    private fun Int.toVersion(): String {
        return if (this < 9) "0$this" else "$this"
    }

    private const val major = 2
    private const val minor = 0
    private const val patch = 0
    private const val candidate = 40

    private const val channel = "alpha"

    const val compileSdk = 33
    const val targetSdk = 33
    const val minSdk = 21

    /**
     * **RR**_X.Y.Z_
     * > **RR** reserved for build flavours and **X.Y.Z** follow the [versionName] convention
     */
    const val versionCode = major.times(1_000_000_000) +
            minor.times(1_000_000) +
            patch.times(1_000) +
            candidate

    /**
     * Naming schema: X.Y.Z-variant##
     * > **X**(Major).**Y**(Minor).**Z**(Patch)
     */
    val versionName = if (candidate > 0)
        "$major.$minor.$patch-$channel${candidate.toVersion()}"
    else
        "$major.$minor.$patch"
}