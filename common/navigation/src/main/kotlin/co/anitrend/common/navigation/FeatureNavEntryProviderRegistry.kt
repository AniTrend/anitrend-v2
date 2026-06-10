/*
 * Copyright (C) 2025 AniTrend
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
package co.anitrend.common.navigation

/**
 * Central registry for Nav3 feature entry providers.
 *
 * Feature modules register their providers at startup (via Koin module definitions),
 * and the app runtime reads them via [all].
 */
object FeatureNavEntryProviderRegistry {
    private val providers = linkedSetOf<FeatureNavEntryProvider>()

    fun register(provider: FeatureNavEntryProvider) {
        providers.add(provider)
    }

    fun all(): List<FeatureNavEntryProvider> = providers.toList()
}
