package co.anitrend.koin

import co.anitrend.core.R
import co.anitrend.core.koin.helper.DynamicFeatureModuleHelper
import co.anitrend.push.PushConnector
import co.anitrend.push.PushRegistrationCoordinator
import co.anitrend.push.UnifiedPushConnector
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


private val variantModule =
    module(createdAtStart = true) {
        factory<PushConnector> {
            UnifiedPushConnector(
                context = androidApplication(),
            )
        }
        factory {
            PushRegistrationCoordinator(
                connector = get(),
                messageForDistributor =
                    androidContext().getString(R.string.application_name),
            )
        }
    }

internal val githubAppModules =
    DynamicFeatureModuleHelper(
        listOf(variantModule),
    )
