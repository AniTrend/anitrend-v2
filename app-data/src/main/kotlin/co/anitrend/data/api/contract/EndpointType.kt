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

package co.anitrend.data.api.contract

import co.anitrend.data.BuildConfig
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl

internal enum class EndpointType(val url: HttpUrl) {
    OAUTH("https://${BuildConfig.apiAuthUrl}".toHttpUrl()),
    GRAPH_QL(BuildConfig.apiUrl.toHttpUrl()),
    RELATION_MOE(BuildConfig.relationUrl.toHttpUrl());

    companion object {
        const val BASE_ENDPOINT_PATH = "/"
    }
}