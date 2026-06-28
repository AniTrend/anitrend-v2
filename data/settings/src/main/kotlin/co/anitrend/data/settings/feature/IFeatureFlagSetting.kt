/*
 * Copyright (C) 2026 AniTrend
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
package co.anitrend.data.settings.feature

import co.anitrend.arch.extension.settings.contract.AbstractSetting

interface IFeatureFlagSetting {
    val featureFlags: AbstractSetting<Set<String>>
}

enum class FeatureFlag(
    val key: String,
) {
    EXPERIMENTAL_COMPOSE_UI("experimental_compose_ui"),
    NAV3_COMPOSE_SHELL("nav3_compose_shell"),
    ;

    companion object {
        fun fromToken(token: String): FeatureFlag? =
            entries.firstOrNull { flag ->
                flag.key.equals(token.trim(), ignoreCase = true) ||
                    flag.name.equals(token.trim(), ignoreCase = true)
            }
    }
}

object FeatureFlags {
    val EMPTY = emptySet<String>()

    fun enabledFlags(flags: Set<String>): Set<FeatureFlag> =
        tokens(flags)
            .mapNotNull(FeatureFlag::fromToken)
            .toSet()

    fun isEnabled(
        flags: Set<String>,
        flag: FeatureFlag,
    ): Boolean = flag in enabledFlags(flags)

    fun setEnabled(
        flags: Set<String>,
        flag: FeatureFlag,
        enabled: Boolean,
    ): Set<String> {
        val enabledFlags = enabledFlags(flags).toMutableSet()
        if (enabled) {
            enabledFlags += flag
        } else {
            enabledFlags -= flag
        }

        val knownTokens = FeatureFlag.entries.filter { it in enabledFlags }.map(FeatureFlag::key)
        val unknownTokens =
            tokens(flags)
                .filter { FeatureFlag.fromToken(it) == null }
                .distinctBy { it.lowercase() }

        return (knownTokens + unknownTokens).toSet()
    }

    private fun tokens(flags: Set<String>): List<String> =
        flags
            .asSequence()
            .map(String::trim)
            .filter(String::isNotEmpty)
            .distinctBy { it.lowercase() }
            .toList()
}
