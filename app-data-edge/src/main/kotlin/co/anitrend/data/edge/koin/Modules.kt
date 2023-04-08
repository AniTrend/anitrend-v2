package co.anitrend.data.edge.koin

import co.anitrend.data.edge.core.api.factory.EdgeApiFactory
import co.anitrend.data.edge.config.koin.edgeConfigModules
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


private val coreModule = module {
    singleOf(::EdgeApiFactory)
}

val edgeModules = module {
    includes(coreModule, edgeConfigModules)
}
