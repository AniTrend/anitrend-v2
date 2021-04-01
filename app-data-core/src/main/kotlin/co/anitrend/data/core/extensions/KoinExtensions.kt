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

package co.anitrend.data.core.extensions

import co.anitrend.data.core.BuildConfig
import co.anitrend.data.core.api.factory.IEndpointFactory
import co.anitrend.data.core.api.provider.RetrofitProvider
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.context.GlobalContext
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope

/**
 * Helper to resolve koin dependencies
 *
 * @param qualifier Help qualify a component
 * @param parameters Help define a DefinitionParameters
 *
 * @return [T]
 */
inline fun <reified T> koinOf(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
): T {
    val koin = GlobalContext.get()
    return koin.get(qualifier, parameters)
}

/**
 * Facade for supplying retrofit interface types
 */
inline fun <reified T> Scope.api(factory: IEndpointFactory): T =
    RetrofitProvider.provide(this, factory).create(T::class.java)

/**
 * Facade for [OkHttpClient.Builder]
 */
fun Scope.defaultBuilder(excludeHeaders: Set<String> = emptySet()): OkHttpClient.Builder {
    val builder = get<OkHttpClient.Builder> {
        parametersOf(
            if (BuildConfig.DEBUG)
                HttpLoggingInterceptor.Level.HEADERS
            else HttpLoggingInterceptor.Level.NONE
        )
    }
    if (BuildConfig.DEBUG)
        builder.addInterceptor(
            get<ChuckerInterceptor> {
                parametersOf(excludeHeaders)
            }
        )
    return builder
}