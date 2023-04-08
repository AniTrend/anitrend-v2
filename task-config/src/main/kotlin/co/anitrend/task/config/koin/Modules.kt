package co.anitrend.task.config.koin

import co.anitrend.core.koin.helper.DynamicFeatureModuleHelper
import co.anitrend.navigation.ConfigTaskRouter
import co.anitrend.task.config.component.ConfigWorker
import co.anitrend.task.config.provider.FeatureProvider
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

private val workManagerModule = module {
    worker { params ->
        ConfigWorker(
            context = androidContext(),
            parameters = params.get(),
            interactor = get(),
        )
    }
}

private val featureModule = module {
    factory<ConfigTaskRouter.Provider> {
        FeatureProvider()
    }
}

internal val moduleHelper = DynamicFeatureModuleHelper(
    listOf(workManagerModule, featureModule)
)
