/*
 * Copyright (C) 2021  AniTrend
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package co.anitrend.task.medialist.component

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import co.anitrend.arch.core.worker.SupportCoroutineWorker
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.data.auth.settings.IAuthenticationSettings
import co.anitrend.data.medialist.GetMediaListCollectionInteractor
import co.anitrend.data.medialist.model.query.MediaListQuery
import co.anitrend.domain.media.enums.MediaType
import kotlinx.coroutines.flow.*

class MediaListSyncWorker(
    context: Context,
    parameters: WorkerParameters,
    private val interactor: GetMediaListCollectionInteractor,
    private val settings: IAuthenticationSettings
) : SupportCoroutineWorker(context, parameters) {

    private val userId by lazy {
        settings.authenticatedUserId.value
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
    override suspend fun doWork(): ListenableWorker.Result {
        val animeQuery = MediaListQuery.Collection(type = MediaType.ANIME, sort = emptyList(), userId = userId)
        val animeDataState = interactor(animeQuery)


        val animeNetworkState = animeDataState.networkState.first { state ->
            state is NetworkState.Success || state is NetworkState.Error
        }

        val mangaQuery = MediaListQuery.Collection(type = MediaType.MANGA, sort = emptyList(), userId = userId)
        val mangaDataState = interactor(mangaQuery)


        val mangaNetworkState = mangaDataState.networkState.first { state ->
            state is NetworkState.Success || state is NetworkState.Error
        }

        return if (animeNetworkState is NetworkState.Success && mangaNetworkState is NetworkState.Success)
            Result.success()
        else Result.failure()
    }
}