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

package co.anitrend.data.android.info

import android.os.Build
import co.anitrend.data.core.device.IDeviceInfo

class DeviceInfo(platformUserAgent: String) : IDeviceInfo {
    override val manufacturer: String = Build.MANUFACTURER
    override val model: String = Build.MODEL
    override val sdkVersion: Int = Build.VERSION.SDK_INT
    override val releaseVersion: String = Build.VERSION.RELEASE
    override val userAgent: String = createUserAgent(platformUserAgent)


    private fun createUserAgent(platformUserAgent: String): String {
        val deviceInfo = "(${Build.MANUFACTURER} ${Build.MODEL})"
        val buildInfo = "${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})"
        val cpuArchitecture = Build.SUPPORTED_ABIS.firstOrNull()

        return "$platformUserAgent $deviceInfo $buildInfo $cpuArchitecture"
    }
}
