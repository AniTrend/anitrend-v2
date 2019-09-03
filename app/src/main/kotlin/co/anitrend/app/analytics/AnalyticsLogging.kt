/*
 * Copyright (C) 2019  AniTrend
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

package co.anitrend.app.analytics

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import co.anitrend.app.BuildConfig
import co.anitrend.data.util.Settings
import com.crashlytics.android.Crashlytics
import com.google.firebase.analytics.FirebaseAnalytics
import io.fabric.sdk.android.Fabric
import co.anitrend.arch.core.analytic.contract.ISupportAnalytics
import co.anitrend.arch.extension.empty
import timber.log.Timber

class AnalyticsLogging(
    context: Context,
    settings: Settings
): Timber.Tree(), ISupportAnalytics {

    private var fabric: Fabric? = null
    private val analytics: FirebaseAnalytics =
        FirebaseAnalytics.getInstance(context).apply {
            setAnalyticsCollectionEnabled(
                settings.isAnalyticsEnabled
            )
        }

    init {
        if (settings.isCrashlyticsEnabled)
            fabric = Fabric.Builder(context).kits(Crashlytics())
                .appIdentifier(BuildConfig.BUILD_TYPE)
                .build().let { Fabric.with(it) }
    }

    /**
     * Write a log message to its destination. Called for all level-specific methods by default.
     *
     * @param priority Log level. See [Log] for constants.
     * @param tag Explicit or inferred tag. May be `null`.
     * @param message Formatted log message. May be `null`, but then `t` will not be.
     * @param throwable Accompanying exceptions. May be `null`, but then `message` will not be.
     */
    override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) {
        if (priority < Log.INFO)
            return

        runCatching {
            Crashlytics.setInt(PRIORITY, priority)
            Crashlytics.setString(TAG, tag)
            Crashlytics.setString(MESSAGE, message)
        }.exceptionOrNull()?.printStackTrace()

        when (throwable) {
            null -> log(priority, tag, message)
            else -> logException(throwable)
        }
    }

    override fun logCurrentScreen(context: FragmentActivity, tag : String) {
        runCatching {
                fabric?.currentActivity = context
                analytics.setCurrentScreen(context, tag, null)
                logCurrentState(tag, context.intent.extras)
        }.exceptionOrNull()?.printStackTrace()
    }

    override fun logCurrentState(tag: String, bundle: Bundle?) {
        runCatching {
            analytics.logEvent(tag, bundle)
        }.exceptionOrNull()?.printStackTrace()
    }

    override fun logException(throwable: Throwable) {
        runCatching {
            Crashlytics.logException(throwable)
        }.exceptionOrNull()?.printStackTrace()
    }

    override fun log(priority: Int, tag: String?, message: String) {
        runCatching {
            Crashlytics.log(priority, tag, message)
        }.exceptionOrNull()?.printStackTrace()
    }

    override fun clearUserSession() {
        runCatching {
            Crashlytics.setUserIdentifier(String.empty())
        }.exceptionOrNull()?.printStackTrace()
    }

    override fun setCrashAnalyticUser(userIdentifier: String) {
        runCatching {
            Crashlytics.setUserIdentifier(userIdentifier)
        }.exceptionOrNull()?.printStackTrace()
    }

    override fun resetAnalyticsData() {
        runCatching {
            analytics.resetAnalyticsData()
        }.exceptionOrNull()?.printStackTrace()
    }

    companion object {
        private const val PRIORITY = "priority"
        private const val TAG = "tag"
        private const val MESSAGE = "message"
    }
}