/*
 * Copyright (C) 2024 AniTrend
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

import android.content.Context
import android.os.Build
import co.anitrend.data.android.BuildConfig
import co.anitrend.data.core.app.IAppInfo
import java.util.Locale

class AppInfo(context: Context) : IAppInfo {
    override val locale: String
        get() = Locale.getDefault().toLanguageTag()
    override val version: String = versionName(context)
    override val source: String = installationSource(context)
    override val code: String = versionCode(context)
    override val label: String = applicationLabel(context)
    override val buildType: String = BuildConfig.BUILD_TYPE

    private fun versionCode(context: Context): String {
        val packageManager = context.packageManager
        val packageName = context.packageName
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.longVersionCode.toString()
        } else {
            @Suppress("DEPRECATION")
            packageInfo.versionCode.toString()
        }
    }

    private fun versionName(context: Context): String {
        val packageManager = context.packageManager
        val packageName = context.packageName
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        return packageInfo.versionName
    }

    private fun installationSource(context: Context): String {
        val packageManager = context.packageManager
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R ->
                packageManager.getInstallSourceInfo(context.packageName).installingPackageName
            else ->
                @Suppress("DEPRECATION")
                packageManager.getInstallerPackageName(context.packageName)
        } ?: "StandAloneInstall"
    }

    private fun applicationLabel(context: Context): String {
        val applicationInfo = context.applicationInfo
        return when (val applicationLabelResource = applicationInfo.labelRes) {
            0x0 -> applicationInfo.nonLocalizedLabel.toString()
            else -> context.getString(applicationLabelResource)
        }
    }
}
