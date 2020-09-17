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

package co.anitrend.core.koin

import android.net.ConnectivityManager
import android.os.Build
import android.os.PowerManager
import co.anitrend.arch.core.model.IStateLayoutConfig
import co.anitrend.arch.extension.dispatchers.SupportDispatchers
import co.anitrend.arch.extension.ext.isLowRamDevice
import co.anitrend.arch.extension.ext.systemServiceOf
import co.anitrend.arch.extension.util.contract.ISupportDateHelper
import co.anitrend.arch.extension.util.date.SupportDateHelper
import co.anitrend.arch.ui.view.widget.model.StateLayoutConfig
import co.anitrend.core.R
import co.anitrend.core.android.controller.AndroidPowerController
import co.anitrend.core.android.controller.contract.PowerController
import co.anitrend.core.coil.fetch.RequestImageFetcher
import co.anitrend.core.coil.mapper.RequestImageMapper
import co.anitrend.core.helper.StorageHelper
import co.anitrend.core.settings.Settings
import co.anitrend.core.settings.common.cache.ICacheSettings
import co.anitrend.core.util.config.ConfigurationUtil
import co.anitrend.core.util.locale.LocaleHelper
import co.anitrend.core.util.theme.ThemeHelper
import co.anitrend.data.arch.di.dataModules
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import coil.fetch.VideoFrameFileFetcher
import coil.fetch.VideoFrameUriFetcher
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.parameter.parametersOf
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module
import org.ocpsoft.prettytime.PrettyTime
import timber.log.Timber
import java.util.concurrent.TimeUnit

private val coreModule = module {
    factory {
        Settings(
            context = androidContext()
        )
    } binds(Settings.BINDINGS)

    single {
        StateLayoutConfig(
            loadingDrawable = R.drawable.ic_support_empty_state,
            errorDrawable = R.drawable.ic_support_empty_state,
            loadingMessage = R.string.label_text_loading,
            retryAction = R.string.label_text_action_retry
        )
    } bind IStateLayoutConfig::class

    factory {
        ConfigurationUtil(
            settings = get(),
            localeHelper = get(),
            themeHelper = get()
        )
    }

    single {
        SupportDispatchers()
    }

    factory {
        SupportDateHelper()
    } bind ISupportDateHelper::class

    factory {
        val context = androidContext()
        AndroidPowerController(
            context = context,
            powerManager = context.systemServiceOf<PowerManager>(),
            connectivityManager = context.systemServiceOf<ConnectivityManager>(),
            connectivitySettings = get()
        )
    } bind PowerController::class

    factory {
        val localeHelper = get<LocaleHelper>()
        PrettyTime(localeHelper.locale)
    }
}

private val configurationModule = module {
    single {
        LocaleHelper(
            settings = get()
        )
    }

    single {
        ThemeHelper(
            settings = get()
        )
    }

    factory {
        RequestImageMapper(
            powerController = get()
        )
    }

    factory {
        val context = androidContext()
        val isLowRamDevice = context.isLowRamDevice()
        val memoryLimit = if (isLowRamDevice) 0.15 else 0.35

        val client = get<OkHttpClient.Builder> {
            parametersOf(
                HttpLoggingInterceptor.Level.HEADERS
            )
        }.cache(
            Cache(
                StorageHelper.getImageCache(
                    context = androidContext()
                ),
                StorageHelper.getStorageUsageLimit(
                    context = androidContext(),
                    settings = object : ICacheSettings {
                        override var usageRatio = 0.15f
                    }
                )
            )
        ).build()

        val dispatchers = get<SupportDispatchers>()
        val loader = ImageLoader.Builder(context)
            .availableMemoryPercentage(memoryLimit)
            .bitmapPoolPercentage(memoryLimit)
            .dispatcher(dispatchers.io)
            .okHttpClient {
                client
            }.componentRegistry {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                    add(ImageDecoderDecoder())
                else
                    add(GifDecoder())
                add(RequestImageFetcher(client, get()))
                add(SvgDecoder(context))
                add(VideoFrameFileFetcher(context))
                add(VideoFrameUriFetcher(context))
            }

        if (!isLowRamDevice)
            loader.crossfade(360)
        else
            loader.allowRgb565(true)

        loader.build()
    }
}


internal val coreModules = listOf(
    coreModule, configurationModule
) + dataModules