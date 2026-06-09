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
package co.anitrend.data.settings.push

import co.anitrend.arch.extension.settings.contract.AbstractSetting

/**
 * App-level UnifiedPush transport state.
 *
 * AniTrend currently maintains a single active UnifiedPush registration. The
 * endpoint and its owning instance are persisted together so endpoint rotation
 * can be acknowledged without reaching into raw SharedPreferences from the
 * push service implementation.
 */
interface IUnifiedPushSettings {
    val unifiedPushEndpoint: AbstractSetting<String?>
    val unifiedPushInstance: AbstractSetting<String?>

    fun endpointFor(instance: String): String? =
        unifiedPushEndpoint.value.takeIf {
            unifiedPushInstance.value == instance
        }

    fun updateEndpoint(
        instance: String,
        endpoint: String,
    ) {
        unifiedPushInstance.value = instance
        unifiedPushEndpoint.value = endpoint
    }

    fun clearEndpoint(instance: String) {
        if (unifiedPushInstance.value == instance) {
            unifiedPushInstance.value = null
            unifiedPushEndpoint.value = null
        }
    }
}
