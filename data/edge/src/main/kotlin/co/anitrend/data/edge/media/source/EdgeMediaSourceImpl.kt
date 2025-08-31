package co.anitrend.data.edge.media.source

import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.arch.request.callback.RequestCallback
import co.anitrend.data.android.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.android.cleaner.contract.IClearDataHelper
import co.anitrend.data.android.extensions.deferred
import co.anitrend.data.edge.media.datasource.local.EdgeMediaLocalSource
import co.anitrend.data.edge.media.datasource.remote.EdgeMediaRemoteSource
import co.anitrend.data.edge.media.source.contract.EdgeMediaSource
import co.anitrend.data.edge.media.EdgeMediaController
import io.github.wax911.library.model.request.QueryContainerBuilder
import kotlinx.coroutines.CoroutineDispatcher
import co.anitrend.domain.media.entity.Media
import co.anitrend.data.edge.media.mapper.EdgeMediaEntityConverter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

internal class EdgeMediaSourceImpl(
    private val remoteSource: EdgeMediaRemoteSource,
    private val localSource: EdgeMediaLocalSource,
    private val controller: EdgeMediaController,
    private val converter: EdgeMediaEntityConverter,
    private val clearDataHelper: IClearDataHelper,
    override val dispatcher: ISupportDispatcher,
    override val cachePolicy: ICacheStorePolicy,
) : EdgeMediaSource() {
    override fun observable(id: Int): Flow<Media> =
        localSource
            .mediaById(id)
            .flowOn(dispatcher.io)
            .filterNotNull()
            .map(converter::convertFrom)
            .distinctUntilChanged()
            .flowOn(dispatcher.computation)

    override suspend fun getMediaById(callback: RequestCallback): Boolean {
        val deferred =
            deferred {
                // cacheIdentity is set in operator fun invoke(id)
                val mediaId = (cacheIdentity.id).toInt()
                val queryBuilder = QueryContainerBuilder().apply { putVariable("id", mediaId) }
                remoteSource.getMediaById(queryBuilder)
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
        clearDataHelper(context = context, action = localSource::clear)
    }
}
