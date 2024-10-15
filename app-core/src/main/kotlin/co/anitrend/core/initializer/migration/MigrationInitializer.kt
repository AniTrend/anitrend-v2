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
package co.anitrend.core.initializer.migration

import android.content.Context
import androidx.startup.Initializer
import co.anitrend.core.android.settings.Settings
import co.anitrend.core.initializer.contract.AbstractCoreInitializer
import co.anitrend.core.initializer.logger.TimberInitializer
import co.anitrend.core.migration.MigrationManager

/**
 * Migration helper for managing upgrades from one version of the application to another
 */
class MigrationInitializer : AbstractCoreInitializer<Unit>() {
    /**
     * Initializes and a component given the application [Context]
     *
     * @param context The application context.
     */
    override fun create(context: Context) {
        val migrationManager =
            MigrationManager(
                settings = Settings(context),
            )
        migrationManager.applyMigrations(context)
    }

    /**
     * @return A list of dependencies that this [Initializer] depends on. This is
     * used to determine initialization order of [Initializer]s.
     *
     * By default a feature initializer should only start after koin has been initialized
     */
    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf(TimberInitializer::class.java)
    }
}
