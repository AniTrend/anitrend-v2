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

package co.anitrend.core.android.controller

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.PowerManager
import androidx.annotation.RequiresApi
import co.anitrend.arch.extension.ext.systemServiceOf
import co.anitrend.core.android.controller.contract.PowerController
import co.anitrend.core.android.controller.contract.SaveData
import co.anitrend.core.android.controller.contract.SaveDataReason
import co.anitrend.core.android.flowOfBroadcast
import co.anitrend.core.android.settings.connectivity.IConnectivitySettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onStart

class AndroidPowerController(
    private val context: Context,
    private val powerManager: PowerManager?,
    private val connectivityManager: ConnectivityManager?,
    private val connectivitySettings: IConnectivitySettings
) : PowerController {

    override fun shouldSaveDataFlow(ignorePreference: Boolean): Flow<SaveData> {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                merge(
                    context.flowOfBroadcast(IntentFilter(PowerManager.ACTION_POWER_SAVE_MODE_CHANGED)),
                    context.flowOfBroadcast(IntentFilter(ConnectivityManager.ACTION_RESTRICT_BACKGROUND_CHANGED))
                ).map {
                    shouldSaveData()
                }.onStart {
                    emit(shouldSaveData())
                }
            }
            else -> {
                context.flowOfBroadcast(
                    IntentFilter(PowerManager.ACTION_POWER_SAVE_MODE_CHANGED)
                ).map {
                    shouldSaveData()
                }.onStart {
                    emit(shouldSaveData())
                }
            }
        }
    }

    override fun shouldSaveData(): SaveData = when {
        connectivitySettings.isDataSaverOn -> {
            SaveData.Enabled(SaveDataReason.PREFERENCE)
        }
        powerManager?.isPowerSaveMode == true -> {
            SaveData.Enabled(SaveDataReason.SYSTEM_POWER_SAVER)
        }
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && isBackgroundDataRestricted() -> {
            SaveData.Enabled(SaveDataReason.SYSTEM_DATA_SAVER)
        }
        else -> SaveData.Disabled
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun isBackgroundDataRestricted(): Boolean {
        return connectivityManager?.restrictBackgroundStatus ==
                ConnectivityManager.RESTRICT_BACKGROUND_STATUS_ENABLED
    }
}