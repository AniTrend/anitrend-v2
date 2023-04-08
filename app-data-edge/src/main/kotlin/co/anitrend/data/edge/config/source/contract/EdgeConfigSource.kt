package co.anitrend.data.edge.config.source.contract

import co.anitrend.arch.data.source.core.SupportCoreDataSource
import co.anitrend.arch.request.callback.RequestCallback
import co.anitrend.data.android.cache.extensions.invoke
import co.anitrend.data.android.cache.model.CacheIdentity
import co.anitrend.data.android.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.edge.config.cache.EdgeConfigCache
import co.anitrend.domain.config.entity.Config
import kotlinx.coroutines.flow.Flow

abstract class EdgeConfigSource : SupportCoreDataSource() {

    protected lateinit var cacheIdentity: CacheIdentity

    protected abstract val cachePolicy: ICacheStorePolicy

    internal abstract fun observable(): Flow<Config>

    protected abstract suspend fun getConfig(
        callback: RequestCallback
    ): Boolean

    operator fun invoke() : Flow<Config> {
        cacheIdentity = EdgeConfigCache.Identity()
        cachePolicy(
            scope = scope,
            requestHelper = requestHelper,
            cacheIdentity = cacheIdentity,
            block = ::getConfig
        )
        return observable()
    }
}
