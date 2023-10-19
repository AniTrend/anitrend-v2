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

package co.anitrend.core.coil.client

import co.anitrend.data.android.network.client.OkHttpCallNetworkClient
import coil.decode.ImageSource
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import timber.log.Timber
import java.io.File

internal class CoilRequestClient(
    private val client: OkHttpClient,
    private val imageCache: File
) : OkHttpCallNetworkClient() {

    data class DataResult(
        val source: ImageSource,
        val contentType: String?
    )

    @Throws(Exception::class)
    suspend inline fun fetch(
        resource: HttpUrl,
        firstDelay: Long = 100,
        maxAttempts: Int = 3,
    ): DataResult {
        val request = Request.Builder()
            .get()
            .url(resource)
            .build()

        val call = client.newCall(request)

        val response = call.execute(
            firstDelay,
            maxAttempts,
            ::defaultShouldRetry
        )

        // headers ignores case
        val contentType = response.header(
            "content-type",
            "image/*"
        )
        Timber.v("$request | mime type: $contentType")

        val body = response.bodyOrThrow()

        return DataResult(
            ImageSource(
                source = body.source(),
                cacheDirectory = imageCache
            ),
            contentType
        )
    }
}
