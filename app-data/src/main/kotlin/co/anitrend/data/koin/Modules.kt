/*
 * Copyright (C) 2019  AniTrend
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

package co.anitrend.data.koin

import android.content.Context
import android.net.ConnectivityManager
import co.anitrend.arch.extension.network.SupportConnectivity
import co.anitrend.arch.extension.systemServiceOf
import co.anitrend.data.BuildConfig
import co.anitrend.data.api.converter.AniGraphConverter
import co.anitrend.data.api.interceptor.AuthInterceptor
import co.anitrend.data.api.interceptor.ClientInterceptor
import co.anitrend.data.auth.AuthenticationHelper
import co.anitrend.data.database.AniTrendStore
import co.anitrend.data.genre.koin.mediaGenreModules
import co.anitrend.data.tag.koin.mediaTagModules
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

private val coreModule = module {
    single {
        AniTrendStore.create(
            applicationContext = androidContext()
        )
    }

    factory {
        AuthenticationHelper(
            settings = get()
        )
    }
}

private val networkModule = module {
    factory {
        AuthInterceptor(
            authenticationHelper = get()
        )
    }

    factory {
        SupportConnectivity(
            connectivityManager = androidContext()
                .systemServiceOf<ConnectivityManager>(
                    Context.CONNECTIVITY_SERVICE
                )
        )
    }

    factory {
        AniGraphConverter.create(
            context = androidContext()
        )
    }

    single {
        val okHttpClientBuilder = OkHttpClient.Builder()
            .readTimeout(35, TimeUnit.SECONDS)
            .connectTimeout(35, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(ClientInterceptor())
            .authenticator(get<AuthInterceptor>())
        when {
            BuildConfig.DEBUG -> {
                val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
                okHttpClientBuilder.addInterceptor(httpLoggingInterceptor)
            }
        }

        Retrofit.Builder().client(
            okHttpClientBuilder.build()
        ).addConverterFactory(
            get<AniGraphConverter>()
        ).baseUrl(
            BuildConfig.apiUrl
        ).build()
    }
}

val dataModules = listOf(
    coreModule, networkModule
) + mediaTagModules + mediaGenreModules