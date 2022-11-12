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

package co.anitrend.core.android.controller.power

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import co.anitrend.arch.extension.ext.flowOfBroadcast
import co.anitrend.core.android.controller.power.contract.IPowerController
import co.anitrend.core.android.controller.power.contract.PowerSaverState
import co.anitrend.data.settings.connectivity.IConnectivitySettings
import co.anitrend.data.settings.power.IPowerSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onStart
import timber.log.Timber

/**
 * Android platform power management controller
 */
internal class AndroidPowerController(
    private val context: Context,
    private val powerManager: PowerManager?,
    private val connectivityManager: ConnectivityManager?,
    private val connectivitySettings: IConnectivitySettings,
    private val powerSettings: IPowerSettings,
) : IPowerController {

    override fun powerSaverStateFlow(ignorePreference: Boolean): Flow<PowerSaverState> {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                merge(
                    context.flowOfBroadcast(
                        IntentFilter(PowerManager.ACTION_POWER_SAVE_MODE_CHANGED)
                    ),
                    context.flowOfBroadcast(
                        IntentFilter(ConnectivityManager.ACTION_RESTRICT_BACKGROUND_CHANGED)
                    )
                ).map {
                    powerSaverState()
                }.onStart {
                    emit(powerSaverState())
                }
            }
            else -> {
                context.flowOfBroadcast(
                    IntentFilter(PowerManager.ACTION_POWER_SAVE_MODE_CHANGED)
                ).map {
                    powerSaverState()
                }.onStart {
                    emit(powerSaverState())
                }
            }
        }
    }

    override fun powerSaverState(): PowerSaverState = when {
        connectivitySettings.isDataSaverOn.value -> {
            PowerSaverState.Enabled(PowerSaverState.Reason.PREFERENCE)
        }
        powerManager?.isPowerSaveMode == true -> {
            PowerSaverState.Enabled(PowerSaverState.Reason.SYSTEM_POWER_SAVER)
        }
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && isBackgroundDataRestricted() -> {
            PowerSaverState.Enabled(PowerSaverState.Reason.SYSTEM_DATA_SAVER)
        }
        else -> PowerSaverState.Disabled
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun disableBatteryOptimization() {
        val packageUri = "package:${context.packageName}"
        val ignoresOptimization = powerManager?.isIgnoringBatteryOptimizations(packageUri)
        if (ignoresOptimization == true) {
            runCatching {
                val intent = Intent().apply {
                    action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                    data = packageUri.toUri()
                }
                context.startActivity(intent)
            }.onFailure {
                Timber.w(it)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun isBackgroundDataRestricted(): Boolean {
        return connectivityManager?.restrictBackgroundStatus ==
                ConnectivityManager.RESTRICT_BACKGROUND_STATUS_ENABLED
    }
}