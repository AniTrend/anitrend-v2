package co.anitrend.task.config.initializer

import android.content.Context
import androidx.startup.Initializer
import co.anitrend.core.initializer.contract.AbstractTaskInitializer
import co.anitrend.navigation.ConfigTaskRouter

class WorkSchedulerInitializer : AbstractTaskInitializer<Unit>()  {
    /**
     * Initializes and a component given the application [Context]
     *
     * @param context The application context.
     */
    override fun create(context: Context) {
        ConfigTaskRouter.forScheduler().schedule(context)
    }

    /**
     * @return A list of dependencies that this [Initializer] depends on. This is
     * used to determine initialization order of [Initializer]s.
     *
     * By default a feature initializer should only start after koin has been initialized
     */
    override fun dependencies(): List<Class<out Initializer<*>>> {
        return super.dependencies() + listOf(FeatureInitializer::class.java)
    }
}
