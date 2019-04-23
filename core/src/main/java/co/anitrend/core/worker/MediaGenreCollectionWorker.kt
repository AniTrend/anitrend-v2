package co.anitrend.core.worker

import android.content.Context
import androidx.work.WorkerParameters
import co.anitrend.core.api.endpoint.MediaEndPoint
import co.anitrend.core.dao.DatabaseHelper
import co.anitrend.core.extension.getEndPointOf
import co.anitrend.core.model.response.general.media.MediaGenre
import co.anitrend.core.presenter.CorePresenter
import co.anitrend.core.util.graphql.GraphUtil
import io.wax911.support.core.controller.SupportRequestClient
import io.wax911.support.core.worker.SupportWorker
import io.wax911.support.core.wrapper.extension.isSuccessful
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber

class MediaGenreCollectionWorker(
    context: Context,
    workerParameters: WorkerParameters
): SupportWorker<CorePresenter>(context, workerParameters), KoinComponent {

    private val supportRequestClient by inject<SupportRequestClient>()

    private val databaseHelper by inject<DatabaseHelper>()

    /**
     * @return the presenter that will be used by the worker
     */
    override fun initPresenter() = inject<CorePresenter>().value

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
        val mediaGenresCall =
            applicationContext.getEndPointOf<MediaEndPoint>()
                .getMediaGenres(
                    GraphUtil.getDefaultQuery()
                )
        val wrapper = supportRequestClient.executeUsing(mediaGenresCall)
        if (wrapper.isSuccessful()) {
            return wrapper.model?.data?.genreCollection?.let {
                try {
                    val mediaGenreList = it.map { genre ->
                        MediaGenre(genre)
                    }.toTypedArray()
                    databaseHelper.mediaGenreDao().insert(*mediaGenreList)
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