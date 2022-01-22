/*
 * Copyright (C) 2020  AniTrend
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

package co.anitrend.task.user.provider

import co.anitrend.core.android.koinOf
import co.anitrend.data.settings.sync.ISyncSettings
import co.anitrend.navigation.UserTaskRouter
import co.anitrend.task.user.component.UserAccountSyncWorker
import co.anitrend.task.user.component.UserFollowToggleWorker
import co.anitrend.task.user.component.UserStatisticSyncWorker
import co.anitrend.task.user.scheduler.UserAccountScheduler
import co.anitrend.task.user.scheduler.UserStatisticScheduler

internal class FeatureProvider : UserTaskRouter.Provider {
    override fun accountSyncWorker() = UserAccountSyncWorker::class.java

    override fun followToggleWorker() = UserFollowToggleWorker::class.java

    override fun statisticSyncWorker() = UserStatisticSyncWorker::class.java

    override fun accountSyncScheduler() =
        UserAccountScheduler(
            worker = accountSyncWorker(),
            settings = koinOf()
        )

    override fun statisticSyncScheduler() =
        UserStatisticScheduler(
            worker = statisticSyncWorker(),
            settings = koinOf()
        )
}