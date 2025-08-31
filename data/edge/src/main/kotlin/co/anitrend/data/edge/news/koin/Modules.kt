package co.anitrend.data.edge.news.koin

import co.anitrend.data.android.extensions.graphQLController
import co.anitrend.data.edge.core.extensions.aniTrendApi
import co.anitrend.data.edge.news.EdgeNewsController
import co.anitrend.data.edge.news.datasource.remote.EdgeNewsRemoteSource
import co.anitrend.data.edge.news.extensions.newsStore
import co.anitrend.data.edge.news.mapper.EdgeNewsEntityConverter
import co.anitrend.data.edge.news.mapper.EdgeNewsMapper
import co.anitrend.data.edge.news.mapper.EdgeNewsModelConverter
import org.koin.dsl.module

internal val edgeNewsModules = module {
    single<EdgeNewsRemoteSource> { aniTrendApi() }
    single { newsStore().edgeNewsDao() }

    factory { EdgeNewsModelConverter() }
    factory { EdgeNewsEntityConverter() }
    factory {
        EdgeNewsMapper(
            localSource = get(),
            converter = get(),
        )
    }
    factory<EdgeNewsController> {
        graphQLController(
            mapper = get<EdgeNewsMapper>(),
        )
    }

    // PagingSource is constructed per-use in the feature layer with injected dependencies
}
