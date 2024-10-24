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

package co.anitrend.core.extensions

import android.content.Context
import android.view.View
import co.anitrend.arch.extension.ext.snackBar
import co.anitrend.core.R
import co.anitrend.data.auth.settings.IAuthenticationSettings
import co.anitrend.navigation.AuthRouter
import co.anitrend.navigation.MediaListTaskRouter
import co.anitrend.navigation.UserTaskRouter
import co.anitrend.navigation.extensions.startActivity
import co.anitrend.navigation.work.WorkSchedulerController
import com.google.android.material.snackbar.Snackbar

private inline fun taskSchedulersForAuthentication(
    action: (WorkSchedulerController) -> Unit
) = setOf(
    UserTaskRouter.forAccountSyncScheduler(),
    UserTaskRouter.forStatisticSyncScheduler(),
    MediaListTaskRouter.forAnimeScheduler(),
    MediaListTaskRouter.forMangaScheduler(),
).forEach(action)

/**
 * Fires off worker schedules that should run when a user is signed in
 */
fun Context.scheduleAuthenticationWorkers() {
    taskSchedulersForAuthentication { controller ->
        controller.schedule(this)
    }
}

/**
 * Fires off worker schedules that should cancel scheduled workers
 */
fun Context.cancelAuthenticationWorkers() {
    taskSchedulersForAuthentication { controller ->
        controller.cancel(this)
    }
}

/**
 * Performs an authentication check, and displays a snack bar or proceeds with [action]
 *
 * @param settings Authentication settings
 * @param action If authenticated delegate
 */
fun View.runIfAuthenticated(
    settings: IAuthenticationSettings,
    action: () -> Unit
) {
    if (!settings.isAuthenticated.value) {
        snackBar(
            text = R.string.label_text_authentication_required,
            duration = Snackbar.LENGTH_INDEFINITE,
            actionText = R.string.action_login
        ) { snackBar ->
            AuthRouter.startActivity(snackBar.context)
            snackBar.dismiss()
        }.show()
    } else {
        runCatching(action).stackTrace()
    }
}
