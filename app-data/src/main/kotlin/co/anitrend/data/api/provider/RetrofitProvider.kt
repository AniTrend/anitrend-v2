/*
 * Copyright (C) 2020  AniTrend
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

package co.anitrend.data.api.provider

import androidx.collection.LruCache
import co.anitrend.data.BuildConfig
import co.anitrend.data.api.contract.EndpointType
import co.anitrend.data.api.interceptor.GraphAuthenticator
import co.anitrend.data.api.interceptor.GraphClientInterceptor
import co.anitrend.data.auth.helper.AuthenticationHelper
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import retrofit2.Retrofit
import timber.log.Timber

/**
 * Factory to supply types retrofit instances
 */
internal object RetrofitProvider {

    private val moduleTag = javaClass.simpleName
    private val cache = LruCache<EndpointType, Retrofit>(3)

    private fun provideOkHttpClient(type: EndpointType, scope: Scope) : OkHttpClient {
        val builder = scope.get<OkHttpClient.Builder> {
            parametersOf(
                if (BuildConfig.DEBUG)
                    HttpLoggingInterceptor.Level.HEADERS
                else HttpLoggingInterceptor.Level.NONE
            )
        }

        when (type) {
            EndpointType.GRAPH_QL -> {
                Timber.tag(moduleTag).d("""
                    Adding authenticator and request interceptors for request: ${type.name}
                    """.trimIndent()
                )
                builder.authenticator(
                    GraphAuthenticator(
                        authenticationHelper = scope.get()
                    )
                ).addInterceptor(GraphClientInterceptor())
                if (BuildConfig.DEBUG)
                    builder.addInterceptor(
                        scope.get<ChuckerInterceptor> {
                            parametersOf(
                                setOf(AuthenticationHelper.AUTHORIZATION)
                            )
                        }
                    )
            }
            EndpointType.RELATION_MOE -> {
                Timber.tag(moduleTag).d("""
                    Adding request interceptors for request: ${type.name}
                    """.trimIndent()
                )
                if (BuildConfig.DEBUG)
                    builder.addInterceptor(
                        scope.get<ChuckerInterceptor> {
                            parametersOf(
                                emptySet<String>()
                            )
                        }
                    )
            }
            EndpointType.OAUTH, EndpointType.NEWS_RSS  -> {
                Timber.tag(moduleTag).d("""
                    Adding request interceptors for request: ${type.name}
                    """.trimIndent()
                )
                if (BuildConfig.DEBUG)
                    builder.addInterceptor(
                        scope.get<ChuckerInterceptor> {
                            parametersOf(
                                emptySet<String>()
                            )
                        }
                    )
            }
        }

        return builder.build()
    }

    private fun create(endpointType: EndpointType, scope: Scope) = scope.get<Retrofit.Builder>()
        .client(provideOkHttpClient(endpointType, scope))
        .baseUrl(endpointType.url).build()

    /**
     * Provides retrofit instances for the given [type] while avoiding creating an instance every time
     *
     * @param type The type of endpoint, this behaves as a look up key
     * @param scope The dependency injector scope that should be used to resolve internal dependencies
     */
    fun provide(type: EndpointType, scope: Scope): Retrofit {
        val reference = cache.get(type)
        return when {
            reference != null -> {
                Timber.tag(moduleTag).d(
                    "Using cached retrofit instance for endpoint: ${type.name}"
                )
                reference
            }
            else -> {
                Timber.tag(moduleTag).d(
                    "Creating new retrofit instance for endpoint: ${type.name}"
                )
                val retrofit = create(type, scope)
                cache.put(type, retrofit)
                retrofit
            }
        }
    }
}