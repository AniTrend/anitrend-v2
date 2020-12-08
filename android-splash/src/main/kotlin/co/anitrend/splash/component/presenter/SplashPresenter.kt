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

package co.anitrend.splash.component.presenter

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import co.anitrend.core.presenter.CorePresenter
import co.anitrend.core.settings.Settings
import co.anitrend.navigation.GenreTaskRouter
import co.anitrend.navigation.GenreTaskRouter.Provider.Companion.forWorker
import co.anitrend.navigation.MainRouter
import co.anitrend.navigation.OnBoardingRouter
import co.anitrend.navigation.TagTaskRouter
import co.anitrend.navigation.TagTaskRouter.Provider.Companion.forWorker
import co.anitrend.navigation.extensions.startActivity
import kotlinx.coroutines.delay

class SplashPresenter(
    context: Context,
    settings: Settings
) : CorePresenter(context, settings) {

    fun startTagsWorker(activity: FragmentActivity) {
        val worker = TagTaskRouter.forWorker()

        val workRequest = OneTimeWorkRequest.Builder(worker)
            .build()

        WorkManager.getInstance(activity)
            .enqueueUniqueWork(
                worker.simpleName,
                ExistingWorkPolicy.REPLACE,
                workRequest
            )
    }

    fun startGenreWorker(activity: FragmentActivity) {
        val worker = GenreTaskRouter.forWorker()

        val workRequest = OneTimeWorkRequest.Builder(worker)
            .build()

        WorkManager.getInstance(activity)
            .enqueueUniqueWork(
                worker.simpleName,
                ExistingWorkPolicy.REPLACE,
                workRequest
            )
    }

    suspend fun firstRunCheck(activity: FragmentActivity) {
        delay(750)

        if (settings.isNewInstallation.value)
            OnBoardingRouter.startActivity(context)
        else
            MainRouter.startActivity(context)
        activity.finish()
    }
}