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

package co.anitrend.task.tag.provider

import co.anitrend.core.android.koinOf
import co.anitrend.data.settings.sync.ISyncSettings
import co.anitrend.navigation.TagTaskRouter
import co.anitrend.navigation.work.WorkSchedulerController
import co.anitrend.task.tag.component.TagWorker
import co.anitrend.task.tag.scheduler.TagScheduler

internal class FeatureProvider : TagTaskRouter.Provider {
    override fun worker() = TagWorker::class.java

    override fun scheduler() =
        TagScheduler(
            worker = worker(),
            settings = koinOf()
        )
}