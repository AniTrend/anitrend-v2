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

package co.anitrend.data.android.koin

import android.net.ConnectivityManager
import co.anitrend.arch.extension.ext.systemServiceOf
import co.anitrend.arch.extension.network.SupportConnectivity
import co.anitrend.arch.extension.network.contract.ISupportConnectivity
import co.anitrend.data.BuildConfig
import co.anitrend.data.account.koin.accountModules
import co.anitrend.data.status.model.StatusModel
import co.anitrend.data.airing.koin.airingModules
import co.anitrend.data.airing.model.AiringScheduleModel
import co.anitrend.data.core.api.converter.AniTrendConverterFactory
import co.anitrend.data.core.api.converter.request.AniRequestConverter
import co.anitrend.data.android.database.AniTrendStore
import co.anitrend.data.android.database.common.IAniTrendStore
import co.anitrend.data.core.extensions.store
import co.anitrend.data.android.cleaner.ClearDataHelper
import co.anitrend.data.android.cleaner.contract.IClearDataHelper
import co.anitrend.data.android.logger.GraphLogger
import co.anitrend.data.android.logger.OkHttpLogger
import co.anitrend.data.auth.helper.AuthenticationHelper
import co.anitrend.data.auth.helper.contract.IAuthenticationHelper
import co.anitrend.data.auth.koin.authModules
import co.anitrend.data.carousel.koin.carouselModules
import co.anitrend.data.core.api.factory.GraphApiFactory
import co.anitrend.data.core.device.DeviceInfo
import co.anitrend.data.core.device.IDeviceInfo
import co.anitrend.data.customlist.koin.customListModules
import co.anitrend.data.customscore.koin.customScoreModules
import co.anitrend.data.feed.api.factory.IFeedFactory
import co.anitrend.data.feed.koin.feedModules
import co.anitrend.data.genre.koin.genreModules
import co.anitrend.data.jikan.koin.jikanModules
import co.anitrend.data.link.koin.linkModules
import co.anitrend.data.media.koin.mediaModules
import co.anitrend.data.medialist.koin.mediaListModules
import co.anitrend.data.relation.koin.sourceModules
import co.anitrend.data.rank.koin.rankModules
import co.anitrend.data.tag.koin.tagModules
import co.anitrend.data.themes.koin.themesModule
import co.anitrend.data.thexem.koin.theXemModules
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
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

private val coreModule = module {
    single {
        AniTrendStore.create(
            applicationContext = androidContext(),
            migrations = emptyArray()
        )
    } binds IAniTrendStore.BINDINGS
    single<IDeviceInfo> {
        DeviceInfo(
            context = androidContext()
        )
    } bind DeviceInfo::class
    single {
        GraphApiFactory()
    }
    factory<IAuthenticationHelper> {
        AuthenticationHelper(
            authSettings = get(),
            userSettings = get(),
            localSource = store().authDao(),
            mediaListLocalSource = store().mediaListDao(),
            cacheLocalSource = store().cacheDao()
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
                polymorphic(StatusModel::class) {
                    subclass(StatusModel.Progress::class, StatusModel.Progress.serializer())
                    subclass(StatusModel.Message::class, StatusModel.Message.serializer())
                    subclass(StatusModel.Reply::class, StatusModel.Reply.serializer())
                    subclass(StatusModel.Text::class, StatusModel.Text.serializer())
                }
                classDiscriminator = "__type"
            }
            coerceInputValues = true
            isLenient = true
        }
    }
    factory {
        val mimeType = AniRequestConverter.JSON_MIME_TYPE
        AniTrendConverterFactory(
            processor = get(),
            gson = get(),
            jsonFactory = Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
                isLenient = true
            }.asConverterFactory(mimeType),
            graphFactory = get<Json>().asConverterFactory(mimeType),
            xmlFactory = get<IFeedFactory>().provideConverterFactory()
        )
    }
}

private val networkModule = module {
    single<ISupportConnectivity> {
        SupportConnectivity(
            connectivityManager = androidContext()
                .systemServiceOf<ConnectivityManager>()
        )
    }
    single {
        Retrofit.Builder()
            .addConverterFactory(
                get<AniTrendConverterFactory>()
            )
    }
}

private val interceptorModules = module {
    factory { (exclusionHeaders: Set<String>) ->
        ChuckerInterceptor.Builder(context = androidContext())
                .collector(
                    collector = ChuckerCollector(
                        context = androidContext(),
                        showNotification = true,
                        retentionPeriod = RetentionManager.Period.ONE_DAY
                    )
                )
                .maxContentLength(
                    length = 250000L
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
        mediaListModules + feedModules + jikanModules +
        linkModules + rankModules + traktModules + tmdbModules +
        themesModule + theXemModules + customListModules + customScoreModules