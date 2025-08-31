package co.anitrend.data.edge.media.cache

import co.anitrend.data.android.cache.model.CacheIdentity

internal object EdgeMediaCache {
    class Identity(
        override val id: Long,
        override val key: String = "edge_media_detail",
    ) : CacheIdentity
}
