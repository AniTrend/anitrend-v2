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

package co.anitrend.core.migration

import android.content.Context
import co.anitrend.core.BuildConfig
import co.anitrend.core.migration.contract.AbstractMigrationManager
import co.anitrend.core.migration.model.Migration
import co.anitrend.core.migration.model.Migrations
import co.anitrend.core.android.settings.Settings
import timber.log.Timber

/**
 * Migration manager utility helper
 */
internal class MigrationManager(
    override val settings: Settings
) : AbstractMigrationManager() {

    override fun shouldRunMigrations(): Boolean {
        return settings.versionCode.value != BuildConfig.versionCode
    }

    override fun updateVersion(migration: Migration) {
        Timber.d("Saving check point for successful migration from $migration")
        settings.versionCode.value = migration.endVersion
    }

    override fun possibleMigrations(): List<Migration> {
        val lastTrackedVersion = settings.versionCode.value
        Timber.d(
            "Analysing list of possible migrations for last tracked version: $lastTrackedVersion and current version ${BuildConfig.versionCode} - ${BuildConfig.versionName}"
        )
        // TODO: rework migration filter for downgrade and upgrades exclusively
        val minMigrations = Migrations.ALL.filter { migration ->
            IntRange(
                migration.startVersion,
                migration.endVersion
            ).contains(lastTrackedVersion)
        }
        val maxMigrations = Migrations.ALL.filter { migration ->
            IntRange(
                migration.startVersion,
                migration.endVersion
            ).contains(BuildConfig.versionCode)
        }

        return minMigrations + maxMigrations
    }

    /**
     * Instructs migration manager to start possible migrations
     *
     * @throws Throwable If a migration fails
     */
    override fun applyMigrations(context: Context) {
        if (shouldRunMigrations()) {
            val migrations = possibleMigrations()
            Timber.d("Staring pending ${migrations.size} migrations")
            migrations.forEach { migration ->
                runCatching {
                    Timber.d("Migrating from $migration")
                    migration(context, settings)
                }.onFailure { throwable ->
                    Timber.e(throwable, "Failed to apply migration: $migration")
                    throw throwable
                }.onSuccess {
                    updateVersion(migration)
                }
            }
        }
    }
}