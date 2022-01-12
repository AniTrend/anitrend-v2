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
import co.anitrend.navigation.MediaListTaskRouter
import co.anitrend.navigation.UserTaskRouter

/**
 * Fires off worker schedules that should run when a user is signed in
 */
fun Context.scheduleAuthenticationWorkers() {
    UserTaskRouter.forAccountSyncScheduler().schedule(this)
    UserTaskRouter.forStatisticSyncScheduler().schedule(this)
    MediaListTaskRouter.forAnimeScheduler().schedule(this)
    MediaListTaskRouter.forMangaScheduler().schedule(this)
}

/**
 * Fires off worker schedules that should cancel scheduled workers
 */
fun Context.cancelAuthenticationWorkers() {
    UserTaskRouter.forAccountSyncScheduler().cancel(this)
    UserTaskRouter.forStatisticSyncScheduler().cancel(this)
    MediaListTaskRouter.forAnimeScheduler().cancel(this)
    MediaListTaskRouter.forMangaScheduler().cancel(this)
}