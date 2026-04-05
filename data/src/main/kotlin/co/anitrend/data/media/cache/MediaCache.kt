/*
 * Copyright (C) 2021 AniTrend
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
package co.anitrend.data.media.cache

import co.anitrend.data.android.cache.datasource.CacheLocalSource
import co.anitrend.data.android.cache.model.CacheIdentity
import co.anitrend.data.android.cache.model.CacheRequest
import co.anitrend.data.android.cache.repository.CacheStorePolicy
import co.anitrend.domain.media.model.MediaParam
import org.threeten.bp.Instant

internal class MediaCache(
    override val localSource: CacheLocalSource,
    override val request: CacheRequest = CacheRequest.MEDIA,
) : CacheStorePolicy() {
    /**
     * Check if a resource with a given [identity] is permitted to refresh
     *
     * @param identity Unique identifier for the cache item
     * @param expiresAfter defaults to 2 hours
     */
    override suspend fun shouldRefresh(
        identity: CacheIdentity,
        expiresAfter: Instant,
    ): Boolean = isRequestBefore(identity, expiresAfter)

    sealed class Identity : CacheIdentity {
        class Detail(
            val param: MediaParam.Detail,
            override val id: Long = param.id,
            override val key: String = "media_detail",
        ) : Identity()

        class Characters(
            val param: MediaParam.Characters,
            override val id: Long = param.id,
            override val key: String = "media_characters",
        ) : Identity()

        class Staff(
            val param: MediaParam.Staff,
            override val id: Long = param.id,
            override val key: String = "media_staff",
        ) : Identity()

        class Studios(
            val param: MediaParam.Studios,
            override val id: Long = param.id,
            override val key: String = "media_studios",
        ) : Identity()

        class Stats(
            val param: MediaParam.Stats,
            override val id: Long = param.id,
            override val key: String = "media_stats",
        ) : Identity()

        class Relations(
            val param: MediaParam.Relations,
            override val id: Long = param.id,
            override val key: String = "media_relations",
        ) : Identity()

        class Recommendations(
            val param: MediaParam.Recommendations,
            override val id: Long = param.id,
            override val key: String = buildString {
                append("media_recommendations")
                append('_')
                append(param.perPage)
                param.sort
                    ?.takeIf(List<*>::isNotEmpty)
                    ?.joinToString(separator = "_")
                    ?.let { sortKey ->
                        append('_')
                        append(sortKey.lowercase())
                    }
            },
        ) : Identity()

        class Paged(
            override val id: Long = 0,
            override val key: String = "media_paged",
        ) : Identity()

        class Network(
            override val id: Long = 0,
            override val key: String = "media_by_network",
        ) : Identity()
    }
}
