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

package co.anitrend.data.carousel.cache

import co.anitrend.data.android.cache.datasource.CacheLocalSource
import co.anitrend.data.android.cache.model.CacheIdentity
import co.anitrend.data.android.cache.model.CacheRequest
import co.anitrend.data.android.cache.repository.CacheStorePolicy
import org.threeten.bp.Instant

internal class CarouselCache(
    override val localSource: CacheLocalSource,
    override val request: CacheRequest = CacheRequest.CAROUSEL
) : CacheStorePolicy() {

    /**
     * Check if a resource with a given [entityId] is permitted to refresh
     */
    override suspend fun shouldRefresh(
        identity: CacheIdentity,
        expiresAfter: Instant
    ): Boolean = isRequestBefore(identity, expiresAfter)

    enum class Identifier(
        override val id: Long,
        override val key: String
    ) : CacheIdentity {
        ANIME_META(10L, "carousel_anime_meta"),
        MANGA_META(11L, "carousel_manga_meta"),
        ANIME(12L, "carousel_anime"),
        MANGA(13L, "carousel_manga")
    }
}