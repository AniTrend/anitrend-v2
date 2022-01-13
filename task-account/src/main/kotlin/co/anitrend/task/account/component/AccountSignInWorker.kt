/*
 * Copyright (C) 2022  AniTrend
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

package co.anitrend.task.account.component

import android.content.Context
import androidx.work.WorkerParameters
import co.anitrend.arch.core.worker.SupportCoroutineWorker
import co.anitrend.arch.extension.ext.UNSAFE
import co.anitrend.core.android.shortcut.contract.IShortcutController
import co.anitrend.core.android.shortcut.model.Shortcut
import co.anitrend.core.extensions.cancelAuthenticationWorkers
import co.anitrend.core.extensions.scheduleAuthenticationWorkers
import co.anitrend.data.account.AccountInteractor
import co.anitrend.domain.account.model.AccountParam
import co.anitrend.navigation.AccountTaskRouter
import co.anitrend.navigation.extensions.fromWorkerParameters
import timber.log.Timber

class AccountSignInWorker(
    context: Context,
    parameters: WorkerParameters,
    private val interactor: AccountInteractor,
    private val shortcutManager: IShortcutController
) : SupportCoroutineWorker(context, parameters) {

    private val param by lazy(UNSAFE) {
        val data: AccountTaskRouter.Param =
            parameters.fromWorkerParameters(
                AccountTaskRouter.Param
            )

        AccountParam.Activate(
            userId = data.id
        )
    }

    private fun createShortcuts() {
        runCatching {
            shortcutManager.removeAllDynamicShortcuts()
            shortcutManager.createShortcuts(
                Shortcut.AnimeList(),
                Shortcut.MangaList(),
                Shortcut.Notification(),
                Shortcut.Profile(),
                Shortcut.Search()
            )
        }.onFailure { cause: Throwable ->
            Timber.w(cause)
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
        createShortcuts()
        applicationContext.cancelAuthenticationWorkers()
        interactor.signIn(param)
        applicationContext.scheduleAuthenticationWorkers()
        return Result.success()
    }
}