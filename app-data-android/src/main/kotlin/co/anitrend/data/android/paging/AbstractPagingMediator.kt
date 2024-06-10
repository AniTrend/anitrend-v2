package co.anitrend.data.android.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.RemoteMediator
import co.anitrend.arch.data.source.contract.IDataSource
import co.anitrend.arch.data.source.contract.ISource
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.arch.extension.util.DEFAULT_PAGE_SIZE
import co.anitrend.arch.extension.util.pagination.SupportPagingHelper
import co.anitrend.arch.request.AbstractRequestHelper
import co.anitrend.arch.request.extension.createStatusFlow
import co.anitrend.arch.request.helper.RequestHelper
import co.anitrend.arch.request.model.Request

@OptIn(ExperimentalPagingApi::class)
abstract class AbstractPagingMediator<K : Any, V : Any> : RemoteMediator<K, V>(), IDataSource, ISource {

    /**
     * Contract for multiple types of [kotlinx.coroutines.CoroutineDispatcher]
     */
    protected abstract val dispatcher: ISupportDispatcher

    /**
     * Request helper that controls the flow of requests to the implementing data source to avoid
     * multiple requests of the same type before others are completed for this instance
     *
     * @see AbstractRequestHelper
     */
    override val requestHelper by lazy {
        RequestHelper(
            main = dispatcher.main,
            io = dispatcher.io,
        )
    }

    /**
     * Observable for network state during requests that the UI can monitor and
     * act based on state changes
     */
    override val loadState by lazy {
        requestHelper.createStatusFlow()
    }

    /**
     * Representation of the paging state
     */
    protected open val supportPagingHelper =
        SupportPagingHelper(
            isPagingLimit = false,
            pageSize = DEFAULT_PAGE_SIZE,
        )

    /**
     * Invokes [clearDataSource] and should invoke network refresh or reload
     */
    override suspend fun invalidate() {
        clearDataSource(dispatcher.io)
        supportPagingHelper.onPageRefresh()
    }

    /**
     * Performs the necessary operation to invoke a network retry request
     */
    override suspend fun retryFailed() {
        requestHelper.retryWithStatus(
            Request.Status.FAILED,
        ) {}
    }

    /**
     * Invalidate data source and, re-run the last successful or last failed request if applicable
     */
    override suspend fun refresh() {
        invalidate()
    }
}
