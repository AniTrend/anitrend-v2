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

@file:Suppress("DEPRECATION")

package co.anitrend.data.arch.di

import android.net.ConnectivityManager
import androidx.startup.AppInitializer
import co.anitrend.arch.extension.ext.systemServiceOf
import co.anitrend.arch.extension.network.SupportConnectivity
import co.anitrend.arch.extension.network.contract.ISupportConnectivity
import co.anitrend.data.BuildConfig
import co.anitrend.data.account.koin.accountModules
import co.anitrend.data.airing.koin.airingModules
import co.anitrend.data.airing.model.AiringScheduleModel
import co.anitrend.data.api.converter.AniTrendConverterFactory
import co.anitrend.data.api.converter.request.AniRequestConverter
import co.anitrend.data.arch.database.AniTrendStore
import co.anitrend.data.arch.database.common.IAniTrendStore
import co.anitrend.data.arch.extension.db
import co.anitrend.data.arch.helper.data.ClearDataHelper
import co.anitrend.data.arch.helper.data.contract.IClearDataHelper
import co.anitrend.data.arch.logger.GraphLogger
import co.anitrend.data.arch.logger.OkHttpLogger
import co.anitrend.data.arch.storage.StorageController
import co.anitrend.data.arch.storage.contract.IStorageController
import co.anitrend.data.auth.helper.AuthenticationHelper
import co.anitrend.data.auth.koin.authModules
import co.anitrend.data.carousel.koin.carouselModules
import co.anitrend.data.episode.koin.episodeModules
import co.anitrend.data.genre.koin.genreModules
import co.anitrend.data.link.koin.linkModules
import co.anitrend.data.media.koin.mediaModules
import co.anitrend.data.medialist.koin.mediaListModules
import co.anitrend.data.moe.koin.sourceModules
import co.anitrend.data.news.koin.newsModules
import co.anitrend.data.rank.koin.rankModules
import co.anitrend.data.tag.koin.tagModules
import co.anitrend.data.tmdb.koin.tmdbModules
import co.anitrend.data.trakt.koin.traktModules
import co.anitrend.data.user.koin.userModules
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import io.github.wax911.library.annotation.processor.GraphProcessor
import io.github.wax911.library.annotation.processor.contract.AbstractGraphProcessor
import io.github.wax911.library.annotation.processor.plugin.AssetManagerDiscoveryPlugin
import io.github.wax911.library.logger.contract.ILogger
import io.wax911.emojify.initializer.EmojiInitializer
import io.wax911.emojify.manager.IEmojiManager
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.binds
import org.koin.dsl.module
import org.simpleframework.xml.convert.AnnotationStrategy
import org.simpleframework.xml.core.Persister
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.concurrent.TimeUnit

private val coreModule = module {
    single {
        AniTrendStore.create(
            applicationContext = androidContext()
        )
    } binds IAniTrendStore.BINDINGS
    single<IStorageController> {
        StorageController()
    }
    factory {
        AuthenticationHelper(
            settings = get(),
            localSource = db().authDao()
        )
    }
    factory<IClearDataHelper> {
        ClearDataHelper(
            connectivity = get(),
            settings = get()
        )
    }
    factory<IEmojiManager> {
        AppInitializer.getInstance(androidContext())
            .initializeComponent(EmojiInitializer::class.java)
    }
}

private val retrofitModule = module {
    factory<AbstractGraphProcessor> {
        val level = if (BuildConfig.DEBUG) ILogger.Level.VERBOSE else ILogger.Level.ERROR
        GraphProcessor(
            discoveryPlugin = AssetManagerDiscoveryPlugin(
                assetManager = androidContext().assets
            ),
            logger = GraphLogger(level)
        )
    }
    single {
        GsonBuilder()
            .setLenient()
            .create()
    }
    single {
        Json {
            serializersModule = SerializersModule {
                polymorphic(AiringScheduleModel::class) {
                    subclass(AiringScheduleModel.Core::class, AiringScheduleModel.Core.serializer())
                    subclass(AiringScheduleModel.Extended::class, AiringScheduleModel.Extended.serializer())
                }
            }
            coerceInputValues = true
            isLenient = true
        }
    }
    factory {
        AniTrendConverterFactory(
            processor = get(),
            gson = get(),
            jsonFactory = get<Json>().asConverterFactory(
                AniRequestConverter.JSON_MIME_TYPE
            ),
            xmlFactory = SimpleXmlConverterFactory.createNonStrict(
                Persister(AnnotationStrategy())
            )
        )
    }
}

private val networkModule = module {
    factory<ISupportConnectivity> {
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
        ChuckerInterceptor.Builder(
            context = androidContext()
            // The previously created Collector
        )
            .collector(
                collector = ChuckerCollector(
                    context = androidContext(),
                    // Toggles visibility of the push notification
                    showNotification = true,
                    // Allows to customize the retention period of collected data
                    retentionPeriod = RetentionManager.Period.ONE_DAY
                )
                // The max body content length in bytes, after this responses will be truncated.
            )
            .maxContentLength(
                length = 250000L
                // List of headers to replace with ** in the Chucker UI
            )
            .redactHeaders(
                headerNames = exclusionHeaders
            )
            .alwaysReadResponseBody(false)
            .build()
    }
    factory { (interceptorLogLevel: HttpLoggingInterceptor.Level) ->
        OkHttpClient.Builder()
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
) + airingModules + tagModules + genreModules +
        sourceModules + mediaModules + carouselModules +
        authModules + accountModules + userModules +
        mediaListModules + newsModules + episodeModules +
        linkModules + rankModules + traktModules + tmdbModules