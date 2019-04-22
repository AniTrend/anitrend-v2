package co.anitrend.util

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

class AnalyticsUtil private constructor(): Timber.Tree(), ISupportAnalytics {

    private var analytics: FirebaseAnalytics? = null
    private var fabric: Fabric? = null

    /**
     * Write a log message to its destination. Called for all level-specific methods by default.
     *
     * @param priority Log level. See [Log] for constants.
     * @param tag Explicit or inferred tag. May be `null`.
     * @param message Formatted log message. May be `null`, but then `t` will not be.
     * @param t Accompanying exceptions. May be `null`, but then `message` will not be.
     */
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority < Log.INFO)
            return

        Crashlytics.setInt(PRIORITY, priority)
        Crashlytics.setString(TAG, tag)
        Crashlytics.setString(MESSAGE, message)

        when (t) {
            null ->
                log(priority, tag, message)
            else ->
                logException(t)
        }
    }

    /**
     * Sets application global fabric instance, depending on
     * the current application preferences the application may have
     * disabled the current instance from sending any data
     */
    private fun configureCrashAnalytics(context: Context?) {
        context?.also { fabric = Fabric.with(it) }
    }

    private fun configureAnalytics(context: Context?) {
        context?.also {
            analytics = FirebaseAnalytics.getInstance(it)
            analytics?.setAnalyticsCollectionEnabled(true)
        }
    }

    override fun resetAnalyticsData() {
        analytics?.resetAnalyticsData()
    }

    override fun logCurrentScreen(context: FragmentActivity, tag : String) {
        fabric?.currentActivity = context
        analytics?.setCurrentScreen(context, tag, null)
    }

    override fun logCurrentState(tag: String, bundle: Bundle) {
        analytics?.logEvent(tag, bundle)
    }

    override fun logException(throwable: Throwable) =
        Crashlytics.logException(throwable)

    override fun log(priority: Int, tag: String, message: String) =
        Crashlytics.log(priority, tag, message)

    override fun clearUserSession() =
        Crashlytics.setUserIdentifier(String.empty())

    override fun setCrashAnalyticUser(userIdentifier: String) {
        fabric?.also { Crashlytics.setUserIdentifier(userIdentifier) }
    }

    companion object : InstanceCreator<AnalyticsUtil, Context?>({
        AnalyticsUtil().apply {
            try {
                configureCrashAnalytics(it)
                configureAnalytics(it)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }) {
        private const val PRIORITY = "priority"
        private const val TAG = "tag"
        private const val MESSAGE = "message"
    }
}