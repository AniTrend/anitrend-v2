package co.anitrend.core.worker

import android.content.Context
import androidx.work.WorkerParameters
import co.anitrend.core.api.NetworkClient
import co.anitrend.core.api.endpoint.MediaEndPoint
import co.anitrend.core.dao.DatabaseHelper
import co.anitrend.core.extension.getDatabaseHelper
import co.anitrend.core.extension.getEndPointOf
import co.anitrend.core.presenter.SharedPresenter
import co.anitrend.core.util.graphql.GraphUtil
import io.wax911.support.core.controller.SupportRequestClient
import io.wax911.support.core.worker.SupportWorker
import io.wax911.support.core.wrapper.extension.isSuccessful
import timber.log.Timber

class MediaTagWorker(
    context: Context,
    workerParameters: WorkerParameters
): SupportWorker<SharedPresenter>(context, workerParameters) {

    private val supportRequestClient: SupportRequestClient by lazy {
        NetworkClient()
    }

    private val databaseHelper: DatabaseHelper by lazy {
        applicationContext.getDatabaseHelper()
    }

    /**
     * @return the presenter that will be used by the worker
     */
    override fun initPresenter() = SharedPresenter.newInstance(applicationContext)

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
            return wrapper.model?.data?.result?.let {
                try {
                    databaseHelper.mediaTagDao().insert(*it.toTypedArray())
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