
package co.anitrend.task.favourite.component

import android.content.Context
import androidx.work.WorkerParameters
import co.anitrend.arch.core.worker.SupportCoroutineWorker
import co.anitrend.arch.domain.common.IUseCase
import co.anitrend.domain.favourite.model.FavouriteInput
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.navigation.FavouriteTaskRouter
import co.anitrend.navigation.extensions.transform

class MediaFavouriteWorker(
    context: Context,
    parameters: WorkerParameters,
    private val interactor: IUseCase,
) : SupportCoroutineWorker(context, parameters) {

    private val param by parameters.transform<
        FavouriteTaskRouter.Param.MediaToggleParam,
        FavouriteInput
    > {
        when (it.mediaType) {
            MediaType.ANIME -> FavouriteInput.ToggleAnime(animeId = it.id)
            MediaType.MANGA -> FavouriteInput.ToggleManga(mangaId = it.id)
        }
    }

    /**
     * A suspending method to do your work.  This function runs on the coroutine context specified
     * by [coroutineContext].
     *
     * A CoroutineWorker is given a maximum of ten minutes to finish its execution and return a
     * [androidx.work.ListenableWorker.Result].  After this time has expired, the worker will be signalled to
     * stop.
     *
     * @return The [androidx.work.ListenableWorker.Result] of the result of the background work; note that
     * dependent work will not execute if you return [androidx.work.ListenableWorker.Result.failure]
     */
    override suspend fun doWork(): Result {
        //val dataState = interactor(param)
//
        //val loadState = dataState.loadState.first { state ->
        //    state is LoadState.Success || state is LoadState.Error
        //}
//
        //return if (loadState is LoadState.Success)
        //    Result.success()
        //else Result.failure()

        return Result.failure()
    }
}
