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

import co.anitrend.navigation.UserTaskRouter
import co.anitrend.task.user.component.UserFollowWorker
import co.anitrend.task.user.component.UserMessageWorker
import co.anitrend.task.user.component.UserStatisticsWorker

internal class FeatureProvider : UserTaskRouter.Provider {
    override fun followWorker() = UserFollowWorker::class.java

    override fun messageWorker() = UserMessageWorker::class.java

    override fun statisticsWorker() = UserStatisticsWorker::class.java
}