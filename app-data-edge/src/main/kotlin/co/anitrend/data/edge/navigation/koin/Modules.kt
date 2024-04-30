package co.anitrend.data.edge.navigation.koin

import co.anitrend.data.edge.config.extensions.navigationStore
import co.anitrend.data.edge.navigation.converters.EdgeNavigationModelConverter
import co.anitrend.data.edge.navigation.mapper.EdgeNavigationMapper
import org.koin.dsl.module

private val mapperModule = module {
    factory {
        EdgeNavigationMapper(
            localSource = navigationStore().edgeNavigationDao(),
            converter = get(),
        )
    }
}

private val converterModule = module {
    factory {
        EdgeNavigationModelConverter()
    }
}

internal val edgeNavigationModule = module {
    includes(mapperModule, converterModule)
}
