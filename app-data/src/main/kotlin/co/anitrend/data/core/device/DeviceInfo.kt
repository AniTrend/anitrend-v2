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

package co.anitrend.data.core.device

import android.content.Context
import android.os.Build
import co.anitrend.data.BuildConfig

/**
 * Device information holder
 *
 * Thanks `danielceinos/Cooper`
 */
class DeviceInfo(context: Context) : IDeviceInfo {
    override val manufacturer: String = Build.MANUFACTURER
    override val model: String = Build.MODEL
    override val sdkVersion: Int = Build.VERSION.SDK_INT
    override val releaseVersion: String = Build.VERSION.RELEASE
    override val userAgent: String

    init {
        userAgent = createUserAgent(context)
    }

    private fun createUserAgent(context: Context): String {
        val applicationInfo = context.applicationInfo

        val applicationName = when (val applicationLabelResource = applicationInfo.labelRes) {
            0 -> applicationInfo.nonLocalizedLabel.toString()
            else -> context.getString(applicationLabelResource)
        }

        val packageManager = context.packageManager
        val source = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R ->
                packageManager.getInstallSourceInfo(context.packageName).installingPackageName
            else ->
                packageManager.getInstallerPackageName(context.packageName)
        } ?: "StandAloneInstall"

        val versionName = BuildConfig.versionName
        val versionCode = BuildConfig.versionCode

        return "$applicationName / $versionName($versionCode); $source; ($manufacturer; $model; SDK $sdkVersion; Android $releaseVersion)"
    }
}
