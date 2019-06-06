package co.anitrend.app.koin

import co.anitrend.app.util.AnalyticsUtil
import io.wax911.support.core.analytic.contract.ISupportAnalytics
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModules = module {
    factory<ISupportAnalytics> {
        AnalyticsUtil(
            context = androidContext()
        )
    }
}