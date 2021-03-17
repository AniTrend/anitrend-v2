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

package co.anitrend.core.migration.contract

import co.anitrend.core.migration.model.Migration
import co.anitrend.core.android.settings.Settings

internal abstract class AbstractMigrationManager : IMigrationManager {
    protected abstract val settings: Settings

    /**
     * Checks if the application has been updated
     */
    abstract fun shouldRunMigrations(): Boolean

    /**
     * Updates the underlying [settings] with the passed [migration],
     * effectively bumping the version for all pass phases
     */
    abstract fun updateVersion(migration: Migration)

    /**
     * Retrieves a list of all possible migrations for this installed version
     * against a list of all migrations that exist for the app
     *
     * @param previousVersion The last tracked version to have been installed
     * @param currentVersion The currently installed, running version
     */
    abstract fun possibleMigrations(previousVersion: Int, currentVersion: Int): List<Migration>
}