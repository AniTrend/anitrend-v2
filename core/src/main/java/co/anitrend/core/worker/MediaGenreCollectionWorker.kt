package co.anitrend.core.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import co.anitrend.core.presenter.CorePresenter
import co.anitrend.data.api.endpoint.MediaEndPoint
import co.anitrend.data.extension.getEndPointOf
import co.anitrend.data.source.media.MediaGenreDataSource
import io.wax911.support.core.worker.SupportWorker
import io.wax911.support.data.factory.contract.IRetrofitFactory
import io.wax911.support.data.model.contract.SupportStateType
import io.wax911.support.extension.util.SupportConnectivityHelper
import org.koin.core.KoinComponent
import org.koin.core.inject

class MediaGenreCollectionWorker(
    context: Context,
    workerParameters: WorkerParameters
): CoroutineWorker(context, workerParameters), KoinComponent {

    private val connectivityHelper by inject<SupportConnectivityHelper>()
    private val retroFactory by inject<IRetrofitFactory>()

    /**
     * A suspending method to do your work.  This function runs on the coroutine context specified
     * by [coroutineContext].
     * <p>
     * A CoroutineWorker is given a maximum of ten minutes to finish its execution and return a
     * [ListenableWorker.Result].  After this time has expired, the worker will be signalled to
     * stop.
     *
     * @return The [ListenableWorker.Result] of the result of the background work; note that
     * dependent work will not execute if you return [ListenableWorker.Result.failure]
     */
    override suspend fun doWork(): Result {
        if (connectivityHelper.isConnected) {
            // TODO: Implement a checking mechanism for the refresh interval for genres to
            //  avoid refreshing the database every time on request
            val endpoint = retroFactory.getEndPointOf<MediaEndPoint>()
            val mediaTagDataSource = MediaGenreDataSource(endpoint)

            val networkResult = mediaTagDataSource.startRequestForType()

            if (networkResult.code == SupportStateType.CONTENT)
                return Result.success()
            return Result.failure()
        }
        return Result.retry()
    }

    companion object {
        const val TAG = "MediaGenreCollectionWorker"
    }
}