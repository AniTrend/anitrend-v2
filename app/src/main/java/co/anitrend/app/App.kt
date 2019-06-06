package co.anitrend.app

import android.app.Application
import androidx.work.Configuration
import co.anitrend.app.util.AnalyticsUtil
import co.anitrend.app.koin.appModules
import co.anitrend.core.koin.coreModules
import co.anitrend.core.koin.corePresenterModules
import co.anitrend.core.koin.coreViewModelModules
import co.anitrend.data.koin.dataModules
import co.anitrend.data.koin.dataNetworkModules
import co.anitrend.data.koin.dataRepositoryModules
import io.wax911.support.core.analytic.contract.ISupportAnalytics
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class App: Application(), Configuration.Provider {

    val analyticsUtil by inject<ISupportAnalytics>()

    /** [Koin](https://insert-koin.io/docs/2.0/getting-started/)
     * Initializes Koin dependency injection
     */
    private fun initializeDependencyInjection() {
        startKoin{
            androidLogger()
            androidContext(
                applicationContext
            )
            modules(
                /** Application dependencies */
                appModules,
                /** Core dependencies */
                coreModules,
                coreViewModelModules,
                corePresenterModules,
                /** Data dependencies */
                dataModules,
                dataNetworkModules,
                dataRepositoryModules
            )
        }
    }

    /**
     * Timber logging tree depending on the build type we plant the appropriate tree
     */
    private fun plantLoggingTree() {
        when (BuildConfig.DEBUG) {
            true -> Timber.plant(Timber.DebugTree())
            else -> Timber.plant(analyticsUtil as AnalyticsUtil)
        }
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
        initializeDependencyInjection()
        plantLoggingTree()
    }

    /**
     * @return The [Configuration] used to initialize WorkManager
     */
    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .build()
    }
}