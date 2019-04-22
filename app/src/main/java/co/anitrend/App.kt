package co.anitrend

import android.app.Application
import co.anitrend.util.AnalyticsUtil
import io.wax911.support.core.analytic.contract.ISupportAnalytics
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import timber.log.Timber

class App: Application() {

    val analyticsUtil: ISupportAnalytics by lazy {
        AnalyticsUtil.newInstance(this)
    }

    init {
        EventBus.builder().logNoSubscriberMessages(BuildConfig.DEBUG)
            .sendNoSubscriberEvent(BuildConfig.DEBUG)
            .sendSubscriberExceptionEvent(BuildConfig.DEBUG)
            .throwSubscriberException(BuildConfig.DEBUG)
            .installDefaultEventBus()
    }

    /**
     * Called when the application is starting, before any activity, service,
     * or receiver objects (excluding content providers) have been created.
     *
     *
     * Implementations should be as quick as possible (for example using
     * lazy initialization of state) since the time spent in this function
     * directly impacts the performance of starting the first activity,
     * service, or receiver in a process.
     *
     *
     * If you override this method, be sure to call `super.onCreate()`.
     *
     *
     * Be aware that direct boot may also affect callback order on
     * Android [android.os.Build.VERSION_CODES.N] and later devices.
     * Until the user unlocks the device, only direct boot aware components are
     * allowed to run. You should consider that all direct boot unaware
     * components, including such [android.content.ContentProvider], are
     * disabled until user unlock happens, especially when component callback
     * order matters.
     */
    override fun onCreate() {
        super.onCreate()
        GlobalScope.launch(Dispatchers.IO) {
            when (BuildConfig.DEBUG) {
                true -> Timber.plant(Timber.DebugTree())
                else -> Timber.plant(analyticsUtil as AnalyticsUtil)
            }
        }
    }
}