/*
 * Copyright (C) 2021 AniTrend
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
package co.anitrend.task.medialist.initializer

import android.content.Context
import androidx.startup.Initializer
import co.anitrend.core.android.koinOf
import co.anitrend.core.initializer.contract.AbstractTaskInitializer
import co.anitrend.data.auth.settings.IAuthenticationSettings
import co.anitrend.navigation.MediaListTaskRouter

class WorkSchedulerInitializer : AbstractTaskInitializer<Unit>() {
    /**
     * Initializes and a component given the application [Context]
     *
     * @param context The application context.
     */
    override fun create(context: Context) {
        val settings = koinOf<IAuthenticationSettings>()
        if (settings.isAuthenticated.value) {
            MediaListTaskRouter.forAnimeScheduler().schedule(context)
            MediaListTaskRouter.forMangaScheduler().schedule(context)
        }
    }

    /**
     * @return A list of dependencies that this [Initializer] depends on. This is
     * used to determine initialization order of [Initializer]s.
     *
     * By default a feature initializer should only start after koin has been initialized
     */
    override fun dependencies(): List<Class<out Initializer<*>>> {
        return super.dependencies() + listOf(FeatureInitializer::class.java)
    }
}
