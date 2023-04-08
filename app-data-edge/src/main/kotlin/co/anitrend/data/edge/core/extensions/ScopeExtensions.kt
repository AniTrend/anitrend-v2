package co.anitrend.data.edge.core.extensions

import co.anitrend.data.core.extensions.api
import co.anitrend.data.edge.core.api.factory.EdgeApiFactory
import org.koin.core.scope.Scope

internal inline fun <reified T> Scope.aniTrendApi(): T {
    val provider = get<EdgeApiFactory>()
    return api(provider)
}
