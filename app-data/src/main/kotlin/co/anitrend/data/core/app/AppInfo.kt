package co.anitrend.data.core.app

import android.content.Context
import android.os.Build
import co.anitrend.data.BuildConfig
import java.util.Locale

class AppInfo(context: Context) : IAppInfo {
    override val locale: String
        get() = Locale.getDefault().toLanguageTag()
    override val version: String = BuildConfig.versionName
    override val source: String = installationSource(context)
    override val code: String = BuildConfig.versionCode.toString()
    override val label: String = applicationLabel(context)
    override val buildType: String = BuildConfig.BUILD_TYPE


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