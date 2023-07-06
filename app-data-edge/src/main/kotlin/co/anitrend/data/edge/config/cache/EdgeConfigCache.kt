package co.anitrend.data.edge.config.cache

import co.anitrend.data.android.cache.datasource.CacheLocalSource
import co.anitrend.data.android.cache.model.CacheIdentity
import co.anitrend.data.android.cache.model.CacheRequest
import co.anitrend.data.android.cache.repository.CacheStorePolicy
import org.threeten.bp.Instant

internal class EdgeConfigCache(
    override val localSource: CacheLocalSource,
    override val request: CacheRequest = CacheRequest.CONFIG
) : CacheStorePolicy() {
    /**
     * Check if a resource with a given [identity] is permitted to refresh
     *
     * @param identity Unique identifier for the cache item
     * @param expiresAfter Expiry time fro the cached [identity]
     */
    override suspend fun shouldRefresh(
        identity: CacheIdentity,
        expiresAfter: Instant
    ): Boolean = isRequestBefore(identity, expiresAfter)

    class Identity(
        override val id: Long = 1,
        override val key: String = "edge_config"
    ) : CacheIdentity
}
