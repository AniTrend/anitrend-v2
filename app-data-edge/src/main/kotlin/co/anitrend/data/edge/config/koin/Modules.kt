package co.anitrend.data.edge.config.koin

import co.anitrend.data.android.extensions.cacheLocalSource
import co.anitrend.data.android.extensions.graphQLController
import co.anitrend.data.android.extensions.offline
import co.anitrend.data.edge.config.ConfigRepository
import co.anitrend.data.edge.config.GetConfigInteractor
import co.anitrend.data.edge.config.cache.EdgeConfigCache
import co.anitrend.data.edge.config.converters.EdgeConfigEntityConverter
import co.anitrend.data.edge.config.converters.EdgeConfigModelConverter
import co.anitrend.data.edge.config.converters.EdgeConfigViewEntityConverter
import co.anitrend.data.edge.config.extensions.configStore
import co.anitrend.data.edge.config.extensions.genreStore
import co.anitrend.data.edge.config.extensions.navigationStore
import co.anitrend.data.edge.config.mapper.EdgeConfigMapper
import co.anitrend.data.edge.config.repository.EdgeConfigRepository
import co.anitrend.data.edge.config.source.EdgeConfigSourceImpl
import co.anitrend.data.edge.config.source.contract.EdgeConfigSource
import co.anitrend.data.edge.config.usecase.EdgeConfigInteractor
import co.anitrend.data.edge.core.extensions.aniTrendApi
import co.anitrend.data.edge.genre.mapper.EdgeGenreMapper
import co.anitrend.data.edge.navigation.mapper.EdgeNavigationMapper
import org.koin.dsl.module

private val sourceModule = module {
    factory<EdgeConfigSource> {
        EdgeConfigSourceImpl(
            remoteSource = aniTrendApi(),
            localSource = configStore().edgeConfigDao(),
            controller = graphQLController(
                mapper = get<EdgeConfigMapper>(),
                strategy = offline()
            ),
            converter = get(),
            clearDataHelper = get(),
            dispatcher = get(),
            cachePolicy = get<EdgeConfigCache>()
        )
    }
}

private val converterModule = module {
    factory {
        EdgeConfigModelConverter()
    }
    factory {
        EdgeConfigViewEntityConverter()
    }
    factory {
        EdgeConfigEntityConverter()
    }
}

private val cacheModule = module {
    factory {
        EdgeConfigCache(
            localSource = cacheLocalSource()
        )
    }
}

private val mapperModule = module {
    factory {
        EdgeConfigMapper(
            genreMapper = get(),
            navigationMapper = get(),
            localSource = configStore().edgeConfigDao(),
            converter = get(),
        )
    }
    factory {
        EdgeNavigationMapper(
            localSource = navigationStore().edgeNavigationDao(),
            converter = get()
        )
    }
    factory {
        EdgeGenreMapper(
            localSource = genreStore().edgeGenreDao(),
            converter = get(),
        )
    }
}

private val repositoryModule = module {
    factory<ConfigRepository> {
        EdgeConfigRepository(
            source = get()
        )
    }
}

private val useCaseModule = module {
    factory<GetConfigInteractor> {
        EdgeConfigInteractor(
            repository = get()
        )
    }
}

internal val edgeConfigModules = module {
    includes(
        sourceModule,
        converterModule,
        cacheModule,
        mapperModule,
        useCaseModule,
        repositoryModule
    )
}
