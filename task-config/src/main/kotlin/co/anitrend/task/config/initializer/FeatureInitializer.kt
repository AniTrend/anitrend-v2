package co.anitrend.task.config.initializer

import android.content.Context
import co.anitrend.core.initializer.contract.AbstractFeatureInitializer
import co.anitrend.core.koin.helper.DynamicFeatureModuleHelper.Companion.loadModules
import co.anitrend.task.config.koin.moduleHelper

class FeatureInitializer : AbstractFeatureInitializer<Unit>() {

    /**
     * Initializes and a component given the application [Context]
     *
     * @param context The application context.
     */
    override fun create(context: Context) {
        moduleHelper.loadModules()
    }
}
