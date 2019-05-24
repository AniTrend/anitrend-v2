package co.anitrend.core.worker

import android.content.Context
import androidx.work.WorkerParameters
import co.anitrend.data.api.endpoint.MediaEndPoint
import co.anitrend.data.dao.DatabaseHelper
import co.anitrend.data.extension.getEndPointOf
import co.anitrend.core.presenter.CorePresenter
import co.anitrend.data.util.graphql.GraphUtil
import io.wax911.support.core.controller.SupportRequestClient
import io.wax911.support.core.worker.SupportWorker
import io.wax911.support.core.wrapper.extension.isSuccessful
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber

class MediaTagCollectionWorker(
    context: Context,
    workerParameters: WorkerParameters
): SupportWorker<CorePresenter>(context, workerParameters), KoinComponent {

    override val presenter by inject<CorePresenter>()

    private val supportRequestClient by inject<SupportRequestClient>()

    private val databaseHelper by inject<DatabaseHelper>()


    /**
     * Override this method to do your actual background processing.  This method is called on a
     * background thread - you are required to **synchronously** do your work and return the
     * [androidx.work.ListenableWorker.Result] from this method.  Once you return from this
     * method, the Worker is considered to have finished what its doing and will be destroyed.  If
     * you need to do your work asynchronously on a thread of your own choice, see
     * [androidx.work.ListenableWorker].
     *
     *
     * A Worker is given a maximum of ten minutes to finish its execution and return a
     * [androidx.work.ListenableWorker.Result].  After this time has expired, the Worker will
     * be signalled to stop.
     *
     * @return The [androidx.work.ListenableWorker.Result] of the computation; note that
     * dependent work will not execute if you use
     * [androidx.work.ListenableWorker.Result.failure] or
     * [androidx.work.ListenableWorker.Result.failure]
     */
    override fun doWork(): Result {
        val mediaTagsCall =
            applicationContext.getEndPointOf<MediaEndPoint>()
                .getMediaTags(
                    GraphUtil.getDefaultQuery()
                )
        val wrapper = supportRequestClient.executeUsing(mediaTagsCall)
        if (wrapper.isSuccessful()) {
            return wrapper.model?.data?.mediaTagCollection?.let {
                try {
                    runBlocking {
                        databaseHelper.mediaTagDao().insert(*it.toTypedArray())
                    }
                    Result.success()
                } catch (e: Exception) {
                    Timber.e(e)
                    Result.failure()
                }
            } ?: Result.retry()
        }
        return Result.retry()
    }
}