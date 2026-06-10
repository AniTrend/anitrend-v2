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
package co.anitrend.navigation.nav3

import kotlinx.serialization.Serializable

/**
 * Compatibility key for legacy Fragment-based destinations not yet migrated to Nav3.
 *
 * Renders a Fragment host from Compose, allowing old Fragment destinations to function
 * inside the Compose-first Nav3 shell without needing their own Activity.
 */
@Serializable
data class LegacyFragmentNavKey(
    val destination: String,
    val args: Map<String, String> = emptyMap(),
) : AniTrendNavKey
