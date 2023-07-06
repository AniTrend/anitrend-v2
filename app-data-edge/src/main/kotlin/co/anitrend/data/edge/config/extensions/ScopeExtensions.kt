package co.anitrend.data.edge.config.extensions

import co.anitrend.data.edge.config.datasource.local.IEdgeConfigStore
import co.anitrend.data.edge.home.datasource.local.IEdgeHomeStore
import co.anitrend.data.edge.navigation.datasource.local.IEdgeNavigationStore
import org.koin.core.scope.Scope

internal fun Scope.configStore() =
    get<IEdgeConfigStore>()

internal fun Scope.navigationStore() =
    get<IEdgeNavigationStore>()

internal fun Scope.homeStore() =
    get<IEdgeHomeStore>()
