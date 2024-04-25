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
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.WorkerParameters
import co.anitrend.arch.core.worker.SupportCoroutineWorker
import co.anitrend.core.android.shortcut.contract.IShortcutController
import co.anitrend.core.android.shortcut.model.Shortcut
import co.anitrend.core.extensions.cancelAuthenticationWorkers
import co.anitrend.core.extensions.scheduleAuthenticationWorkers
import co.anitrend.data.account.AccountInteractor
import co.anitrend.data.user.settings.IUserSettings
import co.anitrend.domain.account.model.AccountParam
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.navigation.AccountTaskRouter
import co.anitrend.navigation.MediaListRouter
import co.anitrend.navigation.extensions.asNavPayload
import co.anitrend.navigation.extensions.transform
import kotlinx.coroutines.flow.firstOrNull
import timber.log.Timber

class AccountSignInWorker(
    context: Context,
    parameters: WorkerParameters,
    private val interactor: AccountInteractor,
    private val shortcutController: IShortcutController,
    private val settings: IUserSettings,
) : SupportCoroutineWorker(context, parameters) {

    private val param by parameters.transform<
        AccountTaskRouter.AccountParam,
        AccountParam.Activate
    > { AccountParam.Activate(userId = it.id) }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private suspend fun createShortcuts() {
        val scoreFormat = settings.scoreFormat.flow.firstOrNull() ?: IUserSettings.DEFAULT_SCORE_FORMAT
        runCatching {
            shortcutController.removeAllDynamicShortcuts()
            shortcutController.createShortcuts(
                Shortcut.AnimeList(
                    navPayload = MediaListRouter.MediaListParam(
                        scoreFormat = scoreFormat,
                        type = MediaType.ANIME,
                        userId = param.userId,
                    ).asNavPayload()
                ),
                Shortcut.MangaList(
                    navPayload = MediaListRouter.MediaListParam(
                        scoreFormat = scoreFormat,
                        type = MediaType.MANGA,
                        userId = param.userId,
                    ).asNavPayload()
                ),
                Shortcut.Notification(),
                Shortcut.Profile(),
                Shortcut.Search()
            )
        }.onFailure(Timber::w)
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
        applicationContext.cancelAuthenticationWorkers()
        interactor.signIn(param)
        applicationContext.scheduleAuthenticationWorkers()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            createShortcuts()
        }
        return Result.success()
    }
}
