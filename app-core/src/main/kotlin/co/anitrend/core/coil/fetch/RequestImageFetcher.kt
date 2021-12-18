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
import coil.bitmap.BitmapPool
import coil.decode.DataSource
import coil.decode.Options
import coil.fetch.FetchResult
import coil.fetch.Fetcher
import coil.fetch.SourceResult
import coil.size.Size

internal class RequestImageFetcher(
    private val client: CoilRequestClient,
    private val mapper: RequestImageMapper
) : Fetcher<RequestImage<*>> {

    /**
     * Load the [data] into memory. Perform any necessary fetching operations.
     *
     * @param pool A [BitmapPool] which can be used to request [Bitmap] instances.
     * @param data The data to load.
     * @param size The requested dimensions for the image.
     * @param options A set of configuration options for this request.
     */
    override suspend fun fetch(
        pool: BitmapPool,
        data: RequestImage<*>,
        size: Size,
        options: Options
    ): FetchResult {
        val url = mapper.map(data)
        val result = client.fetch(url)
        return SourceResult(
            source = result.source,
            mimeType = result.contentType,
            dataSource = DataSource.NETWORK
        )
    }

    /**
     * Compute the memory cache key for [data].
     *
     * Items with the same cache key will be treated as equivalent by the [MemoryCache].
     *
     * Returning null will prevent the result of [fetch] from being added to the memory cache.
     */
    override fun key(data: RequestImage<*>): String {
        return mapper.getImageUrlUsing(data)
    }
}