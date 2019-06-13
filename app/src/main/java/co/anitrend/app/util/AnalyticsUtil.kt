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

package co.anitrend.app.util

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.crashlytics.android.Crashlytics
import com.google.firebase.analytics.FirebaseAnalytics
import io.fabric.sdk.android.Fabric
import io.wax911.support.core.analytic.contract.ISupportAnalytics
import io.wax911.support.core.factory.InstanceCreator
import io.wax911.support.extension.empty
import timber.log.Timber

class AnalyticsUtil(context: Context): Timber.Tree(), ISupportAnalytics {

    private val analytics by lazy {
        FirebaseAnalytics.getInstance(context).also {
            it.setAnalyticsCollectionEnabled(true)
        }
    }

    private val fabric by lazy {
        Fabric.with(context)
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

        Crashlytics.setInt(PRIORITY, priority)
        Crashlytics.setString(TAG, tag)
        Crashlytics.setString(MESSAGE, message)

        when (throwable) {
            null -> log(priority, tag, message)
            else -> logException(throwable)
        }
    }

    override fun logCurrentScreen(context: FragmentActivity, tag : String) {
        fabric?.currentActivity = context
        analytics.setCurrentScreen(context, tag, null)
    }

    override fun logCurrentState(tag: String, bundle: Bundle) =
        analytics.logEvent(tag, bundle)


    override fun logException(throwable: Throwable) =
        Crashlytics.logException(throwable)

    override fun log(priority: Int, tag: String?, message: String) =
        Crashlytics.log(priority, tag, message)

    override fun clearUserSession() =
        Crashlytics.setUserIdentifier(String.empty())

    override fun setCrashAnalyticUser(userIdentifier: String) {
        fabric?.also { Crashlytics.setUserIdentifier(userIdentifier) }
    }

    override fun resetAnalyticsData() =
        analytics.resetAnalyticsData()

    companion object {
        private const val PRIORITY = "priority"
        private const val TAG = "tag"
        private const val MESSAGE = "message"
    }
}