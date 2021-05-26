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

package co.anitrend.analytics

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import co.anitrend.data.settings.privacy.IPrivacySettings
import co.anitrend.arch.core.analytic.contract.ISupportAnalytics
import timber.log.Timber

/**
 * Logger, plus analytics for github/fdroid variant
 */
class AnalyticsTree(
    context: Context,
    settings: IPrivacySettings
) : ISupportAnalytics, Timber.Tree() {

    /**
     * Clears any set parameters used for logging
     */
    override fun clearCrashAnalyticsSession() {

    }

    /**
     * Handles logging of an analytic service with the [priority] defaulted to [Log.WARN]
     */
    override fun log(priority: Int, tag: String?, message: String) {

    }

    /**
     * Handles logging the current state of a visited screen
     */
    override fun logCurrentScreen(context: FragmentActivity, tag: String) {

    }

    /**
     * Handles logging the current state of a visited screen using an explicit [bundle]
     */
    override fun logCurrentState(tag: String, bundle: Bundle?) {

    }

    /**
     * Handles logging of exceptions to an analytic service
     */
    override fun logException(throwable: Throwable) {

    }

    /**
     * Set unique identifier for crashlytics, this could be a device model
     * associated with a user name or some other identifier
     */
    override fun setCrashAnalyticIdentifier(identifier: String) {

    }

    /**
     * Write a log message to its destination. Called for all level-specific methods by default.
     *
     * @param priority Log level. See [Log] for constants.
     * @param tag Explicit or inferred tag. May be `null`.
     * @param message Formatted log message. May be `null`, but then `t` will not be.
     * @param t Accompanying exceptions. May be `null`, but then `message` will not be.
     */
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {

    }
}