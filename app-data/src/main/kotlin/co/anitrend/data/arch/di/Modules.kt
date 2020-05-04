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

package co.anitrend.data.arch.di

import android.content.Context
import android.net.ConnectivityManager
import co.anitrend.arch.extension.network.SupportConnectivity
import co.anitrend.arch.extension.systemServiceOf
import co.anitrend.data.BuildConfig
import co.anitrend.data.api.converter.AniTrendConverterFactory
import co.anitrend.data.api.helper.cache.CacheHelper
import co.anitrend.data.arch.database.AniTrendStore
import co.anitrend.data.arch.helper.data.ClearDataHelper
import co.anitrend.data.auth.util.AuthenticationHelper
import co.anitrend.data.genre.koin.mediaGenreModules
import co.anitrend.data.tag.koin.mediaTagModules
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import okhttp3.Cache
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
    factory {
        ClearDataHelper(
            connectivity = get(),
            settings = get()
        )
    }
}

private val networkModule = module {
    factory {
        SupportConnectivity(
            connectivityManager = androidContext()
                .systemServiceOf<ConnectivityManager>(
                    Context.CONNECTIVITY_SERVICE
                )
        )
    }
    factory {
        Retrofit.Builder()
            .addConverterFactory(
                AniTrendConverterFactory.create(
                    context = androidContext()
                )
            )
    }
}

private val interceptorModules = module {
    factory { (exclusionHeaders: Set<String>) ->
        ChuckerInterceptor(
            context = androidContext(),
            // The previously created Collector
            collector = ChuckerCollector(
                context = androidContext(),
                // Toggles visibility of the push notification
                showNotification = true,
                // Allows to customize the retention period of collected data
                retentionPeriod = RetentionManager.Period.ONE_HOUR
            ),
            // The max body content length in bytes, after this responses will be truncated.
            maxContentLength = 250000L,
            // List of headers to replace with ** in the Chucker UI
            headersToRedact = exclusionHeaders
        )
    }
    factory { (interceptorLogLevel: HttpLoggingInterceptor.Level) ->
        val okHttpClientBuilder = OkHttpClient.Builder()
            .cache(
                Cache(
                    androidContext().cacheDir,
                    CacheHelper.MAX_CACHE_SIZE
                )
            )
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)

        when {
            BuildConfig.DEBUG -> {
                val httpLoggingInterceptor = HttpLoggingInterceptor()
                httpLoggingInterceptor.level = interceptorLogLevel
                okHttpClientBuilder.addInterceptor(httpLoggingInterceptor)
            }
        }

        okHttpClientBuilder
    }
}

val dataModules = listOf(
    coreModule,
    networkModule,
    interceptorModules
) + mediaTagModules + mediaGenreModules