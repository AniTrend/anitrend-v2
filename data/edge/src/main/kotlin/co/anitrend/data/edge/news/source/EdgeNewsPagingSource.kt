package co.anitrend.data.edge.news.source

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingSource
import androidx.paging.PagingState
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.arch.request.callback.RequestCallback
import co.anitrend.arch.request.model.Request
import co.anitrend.data.android.cache.extensions.invoke
import co.anitrend.data.android.cache.model.CacheIdentity
import co.anitrend.data.android.cleaner.contract.IClearDataHelper
import co.anitrend.data.android.paging.AbstractPagingMediator
import co.anitrend.data.edge.news.EdgeNewsController
import co.anitrend.data.edge.news.datasource.local.EdgeNewsLocalSource
import co.anitrend.data.edge.news.datasource.remote.EdgeNewsRemoteSource
import co.anitrend.data.edge.news.mapper.EdgeNewsEntityConverter
import co.anitrend.data.edge.news.model.query.NewsConnectionQuery
import co.anitrend.domain.news.entity.News
import io.github.wax911.library.model.request.QueryContainerBuilder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.first

@OptIn(ExperimentalPagingApi::class)
internal class EdgeNewsPagingSource(
    private val cacheIdentity: CacheIdentity,
    private val remoteSource: EdgeNewsRemoteSource,
    private val localSource: EdgeNewsLocalSource,
    private val controller: EdgeNewsController,
    private val converter: EdgeNewsEntityConverter,
    private val clearDataHelper: IClearDataHelper,
    private val query: NewsConnectionQuery,
    override val dispatcher: ISupportDispatcher,
) : AbstractPagingMediator<Int, News>() {
    private var nextCursor: String? = null

    fun observable(factoryQuery: androidx.sqlite.db.SupportSQLiteQuery): Flow<PagingSource<Int, News>> =
        localSource
            .rawFactory(factoryQuery)
            .map { entity -> converter.convertFrom(entity) }
            .asPagingSourceFactory()
            .asFlow()

    fun observable(): Flow<PagingSource<Int, News>> =
        localSource
            .entryFactory()
            .map { entity -> converter.convertFrom(entity) }
            .asPagingSourceFactory()
            .asFlow()

    private suspend fun getNews(requestCallback: RequestCallback) {
        val deferred = co.anitrend.data.android.extensions.deferred {
            val builder = QueryContainerBuilder()
                .apply {
                    // Edge API expects: after/before and limit (we use 'first' for forward paging)
                    nextCursor?.let { putVariable("after", it) }
                    // Use supportPagingHelper.pageSize as first
                    putVariable("first", supportPagingHelper.pageSize)
                    query.search?.let { putVariable("query", it) }
                }
            remoteSource.getNewsConnection(builder)
        }
        controller(deferred, requestCallback) {
            // Determine paging completion based on returned data size and/or "last" cursor
            val items = it.connection.data
            supportPagingHelper.isPagingLimit = items.size < supportPagingHelper.pageSize
            nextCursor = it.connection.last
            it
        }
    }

    private suspend operator fun invoke(requestType: Request.Type = Request.Type.INITIAL): MediatorResult {
        cacheIdentity.invoke(
            paging = supportPagingHelper,
            requestHelper = requestHelper,
            requestType = requestType,
            block = ::getNews,
        )
        val result = loadState.first { it is LoadState.Success || it is LoadState.Error }
        return when (result) {
            is LoadState.Success -> MediatorResult.Success(supportPagingHelper.isPagingLimit)
            is LoadState.Error -> MediatorResult.Error(result.details)
            else -> MediatorResult.Error(UnknownError("No information can be provided"))
        }
    }

    override suspend fun initialize(): InitializeAction {
        // Defer initial to Paging when it calls load(REFRESH)
        nextCursor = null
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun clearDataSource(context: CoroutineDispatcher) {
        clearDataHelper(context = context, action = localSource::clear)
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, co.anitrend.domain.news.entity.News>): MediatorResult =
        when (loadType) {
            LoadType.REFRESH -> {
                // Reset any local state and trigger initial load
                nextCursor = null
                clearDataSource(dispatcher.io)
                invoke(requestType = Request.Type.INITIAL)
            }
            LoadType.PREPEND -> MediatorResult.Success(true)
            LoadType.APPEND -> invoke(requestType = Request.Type.AFTER)
        }
}
