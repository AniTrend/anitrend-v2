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

import android.net.ConnectivityManager
import co.anitrend.arch.extension.ext.systemServiceOf
import co.anitrend.arch.extension.network.SupportConnectivity
import co.anitrend.data.BuildConfig
import co.anitrend.data.airing.koin.airingModules
import co.anitrend.data.api.converter.AniTrendConverterFactory
import co.anitrend.data.api.helper.cache.CacheHelper
import co.anitrend.data.arch.database.AniTrendStore
import co.anitrend.data.arch.database.common.IAniTrendStore
import co.anitrend.data.arch.helper.data.ClearDataHelper
import co.anitrend.data.arch.helper.data.contract.IClearDataHelper
import co.anitrend.data.arch.logger.GraphLogger
import co.anitrend.data.arch.logger.OkHttpLogger
import co.anitrend.data.auth.util.AuthenticationHelper
import co.anitrend.data.carousel.koin.carouselModules
import co.anitrend.data.genre.koin.mediaGenreModules
import co.anitrend.data.media.koin.mediaModules
import co.anitrend.data.source.koin.sourceModules
import co.anitrend.data.tag.koin.mediaTagModules
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.google.gson.GsonBuilder
import io.github.wax911.library.annotation.processor.GraphProcessor
import io.github.wax911.library.annotation.processor.contract.AbstractGraphProcessor
import io.github.wax911.library.annotation.processor.plugin.AssetManagerDiscoveryPlugin
import io.github.wax911.library.logger.contract.ILogger
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.binds
import org.koin.dsl.module
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

private val coreModule = module {
    single {
        AniTrendStore.create(
            applicationContext = androidContext()
        )
    } binds IAniTrendStore.BINDINGS
    factory {
        AuthenticationHelper(
            settings = get()
        )
    }
    factory<IClearDataHelper> {
        ClearDataHelper(
            connectivity = get(),
            settings = get()
        )
    }
}

private val retrofitModule = module {
    factory<AbstractGraphProcessor> {
        val level = if (BuildConfig.DEBUG) ILogger.Level.DEBUG else ILogger.Level.ERROR
        GraphProcessor(
            discoveryPlugin = AssetManagerDiscoveryPlugin(
                assetManager = androidContext().assets
            ),
            logger = GraphLogger(level)
        )
    }
    factory {
        GsonBuilder()
            .setLenient()
            .create()
    }
    factory {
        AniTrendConverterFactory(
            processor = get(),
            gson = get()
        )
    }
}

private val networkModule = module {
    factory {
        SupportConnectivity(
            connectivityManager = androidContext()
                .systemServiceOf<ConnectivityManager>()
        )
    }
    factory {
        Retrofit.Builder()
            .addConverterFactory(
                get<AniTrendConverterFactory>()
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
        OkHttpClient.Builder()
            .cache(
                Cache(
                    androidContext().cacheDir,
                    CacheHelper.MAX_CACHE_SIZE
                )
            )
            .addInterceptor(
                HttpLoggingInterceptor(
                    logger = OkHttpLogger()
                ).apply { level = interceptorLogLevel }
            )
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
    }
}

val dataModules = listOf(
    coreModule,
    retrofitModule,
    networkModule,
    interceptorModules
) + airingModules + mediaTagModules + mediaGenreModules +
        sourceModules + mediaModules + carouselModules