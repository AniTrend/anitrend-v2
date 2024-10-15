/*
 * Copyright (C) 2024 AniTrend
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
package co.anitrend.task.config.provider

import co.anitrend.navigation.ConfigTaskRouter
import co.anitrend.task.config.component.ConfigWorker
import co.anitrend.task.config.scheduler.ConfigScheduler

class FeatureProvider : ConfigTaskRouter.Provider {
    override fun worker() = ConfigWorker::class.java

    override fun scheduler() = ConfigScheduler(worker())
}
