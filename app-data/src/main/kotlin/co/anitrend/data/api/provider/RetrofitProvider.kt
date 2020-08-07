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
import co.anitrend.data.auth.util.AuthenticationHelper
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
    private val retrofitCache = LruCache<EndpointType, Retrofit>(3)

    private fun provideOkHttpClient(endpointType: EndpointType, scope: Scope) : OkHttpClient {
        val builder = scope.get<OkHttpClient.Builder> {
            parametersOf(
                if (BuildConfig.DEBUG)
                    HttpLoggingInterceptor.Level.HEADERS
                else HttpLoggingInterceptor.Level.BASIC
            )
        }

        when (endpointType) {
            EndpointType.GRAPH_QL -> {
                Timber.tag(moduleTag).d("""
                    Adding authenticator and request interceptors for request: ${endpointType.name}
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
                    Adding request interceptors for request: ${endpointType.name}
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
            else -> {

            }
        }

        return builder.build()
    }

    private fun createRetrofit(endpointType: EndpointType, scope: Scope) : Retrofit {
        return scope.get<Retrofit.Builder>()
            .client(
                provideOkHttpClient(
                    endpointType,
                    scope
                )
            )
            .baseUrl(endpointType.url)
            .build()
    }

    fun provideRetrofit(endpointType: EndpointType, scope: Scope): Retrofit {
        val reference = retrofitCache.get(endpointType)
        return if (reference != null) {
            Timber.tag(moduleTag).d(
                "Using cached retrofit instance for endpoint: ${endpointType.name}"
            )
            reference
        }
        else {
            Timber.tag(moduleTag).d(
                "Creating new retrofit instance for endpoint: ${endpointType.name}"
            )
            val retrofit =
                createRetrofit(
                    endpointType,
                    scope
                )
            retrofitCache.put(endpointType, retrofit)
            retrofit
        }
    }
}