package co.anitrend.task.config.provider

import co.anitrend.navigation.ConfigTaskRouter
import co.anitrend.task.config.component.ConfigWorker
import co.anitrend.task.config.scheduler.ConfigScheduler

class FeatureProvider : ConfigTaskRouter.Provider {
    override fun worker() = ConfigWorker::class.java
    override fun scheduler() = ConfigScheduler(worker())
}
