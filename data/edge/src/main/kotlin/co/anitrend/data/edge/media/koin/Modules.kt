package co.anitrend.data.edge.media.koin

import co.anitrend.data.android.extensions.graphQLController
import co.anitrend.data.edge.media.datasource.local.EdgeMediaLocalSource
import co.anitrend.data.edge.media.datasource.local.IEdgeMediaStore
import co.anitrend.data.edge.media.datasource.remote.EdgeMediaRemoteSource
import co.anitrend.data.edge.media.mapper.EdgeMediaMapper
import co.anitrend.data.edge.media.mapper.EdgeMediaModelConverter
import co.anitrend.data.edge.media.mapper.EdgeMediaEntityConverter
import co.anitrend.data.edge.media.repository.EdgeMediaRepository
import co.anitrend.data.edge.media.source.EdgeMediaSourceImpl
import co.anitrend.data.edge.media.source.contract.EdgeMediaSource
import co.anitrend.data.edge.media.EdgeMediaController
import org.koin.dsl.bind
import org.koin.dsl.module

internal val edgeMediaModules = module {
    single { get<IEdgeMediaStore>().edgeMediaDao() } bind EdgeMediaLocalSource::class
    single<EdgeMediaRemoteSource> { get() }

    factory { EdgeMediaModelConverter() }
    factory { EdgeMediaEntityConverter() }
    factory {
        EdgeMediaMapper(
            localSource = get(),
            converter = get(),
        )
    }
    factory<EdgeMediaController> {
        graphQLController(
            mapper = get<EdgeMediaMapper>(),
        )
    }

    factory<EdgeMediaSource> {
        EdgeMediaSourceImpl(
            remoteSource = get(),
            localSource = get(),
            controller = get(),
            converter = get(),
            clearDataHelper = get(),
            dispatcher = get(),
            cachePolicy = get(),
        )
    }

    factory { EdgeMediaRepository(source = get()) }
}
