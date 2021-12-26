/*
 * Copyright (C) 2021  AniTrend
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package co.anitrend.data.core.api.provider

import androidx.collection.lruCache
import co.anitrend.data.core.api.factory.IEndpointFactory
import co.anitrend.data.core.api.factory.contract.IEndpointType
import org.koin.core.scope.Scope
import retrofit2.Retrofit
import timber.log.Timber
import java.util.*

/**
 * Factory to supply types retrofit instances
 */
object RetrofitProvider {

    private val cache = lruCache<IEndpointType, Retrofit>(
        4,
        sizeOf = { key, _ -> key.size },
        onEntryRemoved = { evicted, key, _, _ ->
            Timber.d("Cached retrofit removed:  key -> $key | evicted -> $evicted")
        }
    )

    private fun create(scope: Scope, config: IEndpointFactory) =
        scope.get<Retrofit.Builder>()
            .client(config.okHttpConfig(scope))
            .baseUrl(config.endpointType.url)
            .build()

    /**
     * Provides retrofit instances for the given [type] while avoiding creating an instance every time
     *
     * @param type The type of endpoint, this behaves as a look up key
     * @param scope The dependency injector scope that should be used to resolve internal dependencies
     */
    fun provide(scope: Scope, config: IEndpointFactory): Retrofit {
        val reference = cache.get(config.endpointType)
        return when {
            reference != null -> {
                Timber.d("Using cached retrofit instance for endpoint: $config")
                reference
            }
            else -> {
                Timber.d("Creating new retrofit instance for endpoint: $config")
                val retrofit = create(scope, config)
                cache.put(config.endpointType, retrofit)
                retrofit
            }
        }
    }
}