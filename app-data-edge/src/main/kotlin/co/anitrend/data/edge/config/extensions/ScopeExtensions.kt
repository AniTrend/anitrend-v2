package co.anitrend.data.edge.config.extensions

import co.anitrend.data.edge.config.datasource.local.IEdgeConfigStore
import co.anitrend.data.edge.genre.datasource.IEdgeGenreStore
import co.anitrend.data.edge.navigation.datasource.IEdgeNavigationStore
import org.koin.core.scope.Scope

internal fun Scope.configStore() =
    get<IEdgeConfigStore>()

internal fun Scope.navigationStore() =
    get<IEdgeNavigationStore>()

internal fun Scope.genreStore() =
    get<IEdgeGenreStore>()
