/*
 * Copyright (C) 2019  AniTrend
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

package co.anitrend.core.worker

import android.content.Context
import androidx.work.WorkerParameters
import co.anitrend.core.presenter.CorePresenter
import co.anitrend.data.api.endpoint.MediaEndPoint
import co.anitrend.data.extension.getEndPointOf
import co.anitrend.data.source.media.MediaTagDataSource
import io.wax911.support.core.worker.SupportCoroutineWorker
import io.wax911.support.data.factory.contract.IRetrofitFactory
import io.wax911.support.extension.util.SupportConnectivityHelper
import org.koin.core.KoinComponent
import org.koin.core.inject

class MediaTagCollectionWorker(
    context: Context,
    workerParameters: WorkerParameters
) : SupportCoroutineWorker<CorePresenter>(context, workerParameters), KoinComponent {

    private val connectivityHelper by inject<SupportConnectivityHelper>()
    private val retroFactory by inject<IRetrofitFactory>()

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
        if (connectivityHelper.isConnected) {
            // TODO: Implement a checking mechanism for the refresh interval for genres to
            //  avoid refreshing the database every time on request
            val endpoint = retroFactory.getEndPointOf<MediaEndPoint>()
            val mediaTagDataSource = MediaTagDataSource(endpoint)

            val networkResult = mediaTagDataSource.startRequestForType()

            if (networkResult.isLoaded())
                return Result.success()
            return Result.failure()
        }
        return Result.retry()
    }

    companion object {
        const val TAG = "co.anitrend:MediaTagCollectionWorker"
    }
}