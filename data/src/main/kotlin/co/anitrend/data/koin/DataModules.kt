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
import co.anitrend.data.BuildConfig
import co.anitrend.data.api.converter.AniGraphConverter
import co.anitrend.data.api.endpoint.MediaEndPoint
import co.anitrend.data.api.interceptor.AuthInterceptor
import co.anitrend.data.api.interceptor.ClientInterceptor
import co.anitrend.data.auth.AuthenticationHelper
import co.anitrend.data.dao.AniTrendStore
import co.anitrend.data.util.Settings
import co.anitrend.arch.data.auth.contract.ISupportAuthentication
import co.anitrend.arch.extension.util.SupportConnectivityHelper
import co.anitrend.data.datasource.remote.media.MediaGenreSourceImpl
import co.anitrend.data.datasource.remote.media.MediaTagSourceImpl
import co.anitrend.data.repository.media.MediaGenreRepository
import co.anitrend.data.repository.media.MediaTagRepository
import co.anitrend.data.usecase.media.MediaTagUseCaseImpl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

private val commonDataModules = module {
    factory {
        Settings(
            context = androidContext()
        )
    }

    single {
        AniTrendStore.create(
            applicationContext = androidContext()
        )
    }

    factory<ISupportAuthentication<Request.Builder>> {
        AuthenticationHelper(
            connectivityHelper = get(),
            jsonWebTokenDao = get<AniTrendStore>().jsonWebTokenDao(),
            settings = get()
        )
    }
}

private val networkDataModules = module {
    factory {
        AuthInterceptor(
            authenticationHelper = get()
        )
    }

    factory {
        SupportConnectivityHelper(
            androidContext().getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager?
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

private val dataSourceModule = module {
    factory {
        MediaGenreSourceImpl(
            mediaEndPoint = MediaEndPoint.create(),
            genreDao = get<AniTrendStore>().mediaGenreDao()
        )
    }

    factory {
        MediaTagSourceImpl(
            mediaEndPoint = MediaEndPoint.create(),
            mediaTagDao = get<AniTrendStore>().mediaTagDao()
        )
    }
}

private val repositoryModules = module {
    factory {
        MediaGenreRepository(
            dataSource = get<MediaGenreSourceImpl>()
        )
    }
    factory {
        MediaTagRepository(
            dataSource = get<MediaTagSourceImpl>()
        )
    }
}

private val useCaseModules = module {
    factory {
        MediaTagUseCaseImpl(
            repository = get()
        )
    }
    factory {
        MediaTagUseCaseImpl(
            repository = get()
        )
    }
}

val dataModules = listOf(
    commonDataModules, networkDataModules, dataSourceModule,
    repositoryModules, useCaseModules
)