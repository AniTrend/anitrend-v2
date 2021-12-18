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

package co.anitrend.buildSrc.common

object Versions {

    private const val major = 2
    private const val minor = 0
    private const val patch = 0
    private const val candidate = 38

    private const val channel = "alpha"

    const val compileSdk = 30
    const val targetSdk = 30
    const val minSdk = 21

     /**
      * **RR**_X.Y.Z_
      * > **RR** reserved for build flavours and **X.Y.Z** follow the [versionName] convention
      */
    const val versionCode = major.times(100_000) +
             minor.times(10_000) +
             patch.times(1_000) +
             candidate.times(10)

    /**
     * Naming schema: X.Y.Z-variant##
     * > **X**(Major).**Y**(Minor).**Z**(Patch)
     */
    val versionName = if (candidate > 0)
        "$major.$minor.$patch-$channel$candidate"
    else
        "$major.$minor.$patch"
    const val junit = "4.13.2"

    const val timber = "5.0.1"
    const val threeTenBp = "1.3.1"

    const val debugDB = "1.0.6"
    const val treesSence = "1.0.4"

    const val liquidSwipe = "1.3"

    const val prettyTime = "4.0.4.Final"
    const val scalingImageView = "3.10.0"
    const val serializationConverter = "0.8.0"

    const val jsoup = "1.13.1"

    const val tmdb = "2.3.1"
    const val trakt = "6.9.0"

    const val elements = "1.0.1"

    const val ktlint = "0.40.0"

    const val deeplink = "0.3.1"
}