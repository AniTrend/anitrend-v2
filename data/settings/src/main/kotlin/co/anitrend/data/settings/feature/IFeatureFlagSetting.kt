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
    val featureFlags: AbstractSetting<String>
}

enum class FeatureFlag(
    val key: String,
) {
    EXPERIMENTAL_COMPOSE_UI("experimental_compose_ui"),
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
    const val EMPTY = ""

    fun enabledFlags(csv: String): Set<FeatureFlag> =
        tokens(csv)
            .mapNotNull(FeatureFlag::fromToken)
            .toSet()

    fun isEnabled(
        csv: String,
        flag: FeatureFlag,
    ): Boolean = flag in enabledFlags(csv)

    fun setEnabled(
        csv: String,
        flag: FeatureFlag,
        enabled: Boolean,
    ): String {
        val enabledFlags = enabledFlags(csv).toMutableSet()
        if (enabled) {
            enabledFlags += flag
        } else {
            enabledFlags -= flag
        }

        val knownTokens = FeatureFlag.entries.filter { it in enabledFlags }.map(FeatureFlag::key)
        val unknownTokens =
            tokens(csv)
                .filter { FeatureFlag.fromToken(it) == null }
                .distinctBy { it.lowercase() }

        return (knownTokens + unknownTokens).joinToString(",")
    }

    fun migrateLegacyComposeUi(
        csv: String,
        legacyEnabled: Boolean,
        migrationComplete: Boolean,
    ): String =
        if (!migrationComplete && legacyEnabled) {
            setEnabled(
                csv = csv,
                flag = FeatureFlag.EXPERIMENTAL_COMPOSE_UI,
                enabled = true,
            )
        } else {
            csv
        }

    private fun tokens(csv: String): List<String> =
        csv
            .split(',')
            .map(String::trim)
            .filter(String::isNotEmpty)
}
