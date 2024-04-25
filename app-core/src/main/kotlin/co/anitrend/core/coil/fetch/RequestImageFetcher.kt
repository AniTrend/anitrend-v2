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

package co.anitrend.core.coil.fetch

import co.anitrend.core.android.helpers.image.model.RequestImage
import co.anitrend.core.coil.client.CoilRequestClient
import co.anitrend.core.coil.mapper.RequestImageMapper
import coil.ImageLoader
import coil.decode.DataSource
import coil.fetch.FetchResult
import coil.fetch.Fetcher
import coil.fetch.SourceResult
import coil.key.Keyer
import coil.request.Options
import okhttp3.HttpUrl

internal class RequestImageFetcher private constructor(
    private val client: CoilRequestClient,
    private val mapper: RequestImageMapper,
    private val httpUrl: HttpUrl
) : Fetcher, Keyer<RequestImage<*>> {

    /**
     * Fetch the data provided by [Factory.create] or return 'null' to delegate to the next
     * [Factory] in the component registry.
     */
    override suspend fun fetch(): FetchResult {
        val result = client(httpUrl)
        return SourceResult(
            source = result.source,
            mimeType = result.contentType,
            dataSource = DataSource.NETWORK
        )
    }

    /**
     * Convert [data] into a string key. Return 'null' if this keyer cannot convert [data].
     *
     * @param data The data to convert.
     * @param options The options for this request.
     */
    override fun key(data: RequestImage<*>, options: Options): String {
        return mapper.getImageUrlUsing(data)
    }

    class Factory(
        private val client: CoilRequestClient,
        private val mapper: RequestImageMapper
    ) : Fetcher.Factory<RequestImage<*>> {

        /**
         * Return a [Fetcher] that can fetch [data] or 'null' if this factory cannot create a
         * fetcher for the data.
         *
         * @param data The data to fetch.
         * @param options A set of configuration options for this request.
         * @param imageLoader The [ImageLoader] that's executing this request.
         */
        override fun create(
            data: RequestImage<*>,
            options: Options,
            imageLoader: ImageLoader
        ): Fetcher {
            val url = mapper.map(data, options)
            return RequestImageFetcher(client, mapper, url)
        }
    }
}
