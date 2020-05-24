/*
 * Copyright (C) 2020  AniTrend
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

package co.anitrend.core.controller

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

/**
 * Power management controller
 */
interface PowerController {
    @ExperimentalCoroutinesApi
    fun shouldSaveDataFlow(
        ignorePreference: Boolean
    ): StateFlow<SaveData>
    fun shouldSaveData(): SaveData
}

sealed class SaveData {
    object Disabled : SaveData()
    data class Enabled(
        val reason: SaveDataReason
    ) : SaveData()
}

enum class SaveDataReason {
    PREFERENCE, SYSTEM_DATA_SAVER, SYSTEM_POWER_SAVER
}