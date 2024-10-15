/*
 * Copyright (C) 2022 AniTrend
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
package co.anitrend.config

import android.content.Intent
import co.anitrend.core.config.AbstractDeveloperModeConfig
import co.anitrend.core.extensions.stackTrace
import co.anitrend.data.settings.developer.IDeveloperSettings
import leakcanary.LeakCanary

internal class DeveloperModeConfig(
    override val settings: IDeveloperSettings,
) : AbstractDeveloperModeConfig() {
    init {
        runCatching(::initialize)
            .stackTrace()
    }

    private fun initialize() {
        LeakCanary.showLeakDisplayActivityLauncherIcon(
            settings.showLeakLauncher.value,
        )
        LeakCanary.config =
            LeakCanary.config.copy(
                dumpHeap = settings.automaticHeapDump.value,
            )
    }

    override fun forceHeapDump() {
        LeakCanary.dumpHeap()
    }

    override fun leakDumpIntent(): Intent = LeakCanary.newLeakDisplayActivityIntent()
}
