package co.anitrend.data.airing.source

import androidx.paging.LoadType
import androidx.paging.LoadType.*
import androidx.paging.PagingSource
import androidx.paging.PagingState
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.arch.request.callback.RequestCallback
import co.anitrend.arch.request.model.Request
import co.anitrend.data.airing.AiringSchedulePagedController
import co.anitrend.data.airing.datasource.local.AiringLocalSource
import co.anitrend.data.airing.datasource.remote.AiringRemoteSource
import co.anitrend.data.airing.entity.filter.AiringQueryFilter
import co.anitrend.data.airing.model.query.AiringScheduleQuery
import co.anitrend.data.android.cache.extensions.invoke
import co.anitrend.data.android.cache.model.CacheIdentity
import co.anitrend.data.android.cleaner.contract.IClearDataHelper
import co.anitrend.data.android.extensions.deferred
import co.anitrend.data.android.paging.AbstractPagingMediator
import co.anitrend.data.common.extension.from
import co.anitrend.data.media.converter.MediaEntityViewConverter
import co.anitrend.data.media.datasource.local.MediaLocalSource
import co.anitrend.data.util.GraphUtil.toQueryContainerBuilder
import co.anitrend.domain.media.entity.Media
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.first

internal class AiringSchedulePagingSource(
    private val cacheIdentity: CacheIdentity,
    private val remoteSource: AiringRemoteSource,
    private val localSource: AiringLocalSource,
    private val mediaLocalSource: MediaLocalSource,
    private val controller: AiringSchedulePagedController,
    private val converter: MediaEntityViewConverter,
    private val clearDataHelper: IClearDataHelper,
    private val filter: AiringQueryFilter.Paged,
    private val query: AiringScheduleQuery,
    override val dispatcher: ISupportDispatcher,
) : AbstractPagingMediator<Int, Media>() {

    fun observable(): Flow<PagingSource<Int, Media>> =
        mediaLocalSource
            .rawFactory(filter.build(query.param))
            .map(converter::convertFrom)
            .asPagingSourceFactory()
            .asFlow()


    private suspend fun getAiringSchedule(requestCallback: RequestCallback) {
        val deferred = deferred {
            val queryBuilder = query.toQueryContainerBuilder(
                supportPagingHelper
            )
            remoteSource.getAiringPaged(queryBuilder)
        }

        controller(deferred, requestCallback) {
            supportPagingHelper.from(it.page)
            it
        }
    }

    private suspend operator fun invoke(requestType: Request.Type = Request.Type.INITIAL): MediatorResult {
        cacheIdentity.invoke(
            paging = supportPagingHelper,
            requestHelper = requestHelper,
            requestType = requestType,
            block = ::getAiringSchedule,
        )

        val result = loadState.first {
            it is LoadState.Success || it is LoadState.Error
        }
        return when (result) {
            is LoadState.Success -> MediatorResult.Success(supportPagingHelper.isPagingLimit)
            is LoadState.Error -> MediatorResult.Error(result.details)
            else -> MediatorResult.Error(UnknownError("No information can be provided"))
        }
    }

    override suspend fun initialize(): InitializeAction {
        cacheIdentity.invoke(
            paging = supportPagingHelper,
            requestHelper = requestHelper,
            requestType = Request.Type.INITIAL,
            block = ::getAiringSchedule,
        )
        return InitializeAction.SKIP_INITIAL_REFRESH
    }

    /**
     * Clears data sources (databases, preferences, e.t.c)
     *
     * @param context Dispatcher context to run in
     */
    override suspend fun clearDataSource(context: CoroutineDispatcher) {
        clearDataHelper(context = context, action = localSource::clear)
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Media>
    ): MediatorResult {
        return when (loadType) {
            REFRESH -> {
                clearDataSource(dispatcher.io)
                return invoke(requestType = Request.Type.INITIAL)
            }
            PREPEND -> MediatorResult.Success(true)
            APPEND -> {
                return invoke(requestType = Request.Type.AFTER)
            }
        }
    }
}
