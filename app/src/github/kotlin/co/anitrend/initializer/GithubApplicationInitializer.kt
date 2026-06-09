package co.anitrend.initializer

import android.content.Context
import androidx.startup.Initializer
import co.anitrend.android.core.koinOf
import co.anitrend.core.initializer.contract.AbstractCoreInitializer
import co.anitrend.core.koin.helper.DynamicFeatureModuleHelper.Companion.loadModules
import co.anitrend.koin.githubAppModules
import co.anitrend.push.PushRegistrationCoordinator

class GithubApplicationInitializer : AbstractCoreInitializer<Unit>() {
    /**
     * @return A list of dependencies that this [Initializer] depends on. This is
     * used to determine initialization order of [Initializer]s.
     *
     * For e.g. if a [Initializer] `B` defines another [Initializer] `A` as its dependency,
     * then `A` gets initialized before `B`.
     */
    override fun dependencies(): List<Class<out Initializer<*>>> = listOf(ApplicationInitializer::class.java)

    override fun create(context: Context) {
        githubAppModules.loadModules()
        koinOf<PushRegistrationCoordinator>()
    }
}
