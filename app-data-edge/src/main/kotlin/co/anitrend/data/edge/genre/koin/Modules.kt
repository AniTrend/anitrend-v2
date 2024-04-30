package co.anitrend.data.edge.genre.koin

import co.anitrend.data.edge.config.extensions.genreStore
import co.anitrend.data.edge.genre.converters.EdgeGenreModelConverter
import co.anitrend.data.edge.genre.mapper.EdgeGenreMapper
import org.koin.dsl.module

private val mapperModule = module {
    factory {
        EdgeGenreMapper(
            localSource = genreStore().edgeGenreDao(),
            converter = get()
        )
    }
}

private val converterModule = module {
    factory {
        EdgeGenreModelConverter()
    }
}

internal val edgeGenreModule = module {
    includes(mapperModule, converterModule)
}
