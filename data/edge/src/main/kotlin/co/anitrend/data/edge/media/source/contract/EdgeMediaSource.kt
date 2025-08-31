package co.anitrend.data.edge.media.source.contract

import co.anitrend.arch.request.callback.RequestCallback
import co.anitrend.data.android.cache.extensions.invoke
import co.anitrend.data.android.cache.model.CacheIdentity
import co.anitrend.data.android.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.android.source.AbstractCoreDataSource
import co.anitrend.data.edge.media.cache.EdgeMediaCache
import co.anitrend.domain.media.entity.Media
import kotlinx.coroutines.flow.Flow

internal abstract class EdgeMediaSource : AbstractCoreDataSource() {
    protected lateinit var cacheIdentity: CacheIdentity

    protected abstract val cachePolicy: ICacheStorePolicy

    internal abstract fun observable(id: Int): Flow<Media>

    protected abstract suspend fun getMediaById(callback: RequestCallback): Boolean

    operator fun invoke(id: Int): Flow<Media> {
        cacheIdentity = EdgeMediaCache.Identity(id = id.toLong())
        cachePolicy(
            scope = scope,
            requestHelper = requestHelper,
            cacheIdentity = cacheIdentity,
            block = ::getMediaById,
        )
        return observable(id)
    }
}
