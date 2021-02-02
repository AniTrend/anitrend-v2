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

package co.anitrend.task.news.component

import android.content.Context
import androidx.work.WorkerParameters
import co.anitrend.arch.core.worker.SupportCoroutineWorker
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.core.settings.common.locale.ILocaleSettings
import co.anitrend.core.util.locale.AniTrendLocale.Companion.asLocaleString
import co.anitrend.data.news.NewsInteractor
import co.anitrend.data.news.model.query.NewsQuery
import kotlinx.coroutines.flow.first

class NewsWorker(
    context: Context,
    parameters: WorkerParameters,
    private val interactor: NewsInteractor,
    private val settings: ILocaleSettings
) : SupportCoroutineWorker(context, parameters) {
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
        val locale = settings.locale.value.asLocaleString()
        val dataState = interactor.getPagedNews(NewsQuery(locale))

        val networkState = dataState.networkState.first { state ->
            state is NetworkState.Success || state is NetworkState.Error
        }

        return if (networkState is NetworkState.Success)
            Result.success()
        else Result.failure()
    }
}