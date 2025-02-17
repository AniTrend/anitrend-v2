/*
 * Copyright (C) 2025 AniTrend
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
package co.anitrend.navigation.extensions

import android.net.Uri

enum class DeepLinkType(
    val scheme: String,
    val host: String,
) {
    APP(scheme = "app.anitrend://", host = "action"),
    WEB(scheme = "https://", host = "anilist.co"),
}

@Throws(IllegalArgumentException::class)
fun deepLinkOf(
    path: String,
    type: DeepLinkType,
): Uri {
    if (!path.startsWith("/")) {
        throw IllegalArgumentException("'$path' is not a recognised deep link")
    }
    return Uri.parse("${type.scheme}${type.host}$path")
}
