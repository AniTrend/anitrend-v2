package co.anitrend.data.edge.config.source

import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.arch.request.callback.RequestCallback
import co.anitrend.data.android.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.android.cleaner.contract.IClearDataHelper
import co.anitrend.data.edge.config.EdgeConfigController
import co.anitrend.data.edge.config.converters.EdgeConfigEntityConverter
import co.anitrend.data.edge.config.datasource.local.EdgeConfigLocalSource
import co.anitrend.data.edge.config.datasource.remote.EdgeConfigRemoteSource
import co.anitrend.data.edge.config.entity.EdgeConfigEntity
import co.anitrend.data.edge.config.source.contract.EdgeConfigSource
import co.anitrend.domain.config.entity.Config
import io.github.wax911.library.model.request.QueryContainerBuilder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

internal class EdgeConfigSourceImpl(
    private val remoteSource: EdgeConfigRemoteSource,
    private val localSource: EdgeConfigLocalSource,
    private val controller: EdgeConfigController,
    private val converter: EdgeConfigEntityConverter,
    private val clearDataHelper: IClearDataHelper,
    override val dispatcher: ISupportDispatcher,
    override val cachePolicy: ICacheStorePolicy
) : EdgeConfigSource() {

    override fun observable(): Flow<Config> {
        return localSource.edgeConfig(EdgeConfigEntity.DEFAULT_ID)
            .flowOn(dispatcher.io)
            .filterNotNull()
            .map(converter::convertFrom)
            .distinctUntilChanged()
            .flowOn(dispatcher.computation)
    }

    override suspend fun getConfig(callback: RequestCallback): Boolean {
        val deferred = async {
            remoteSource.getConfig(
                QueryContainerBuilder()
            )
        }

        val result = controller(deferred, callback)

        return result != null
    }

    /**
     * Clears data sources (databases, preferences, e.t.c)
     *
     * @param context Dispatcher context to run in
     */
    override suspend fun clearDataSource(context: CoroutineDispatcher) {
        clearDataHelper(context) {
            cachePolicy.invalidateLastRequest(cacheIdentity)
            localSource.clear()
        }
    }
}
