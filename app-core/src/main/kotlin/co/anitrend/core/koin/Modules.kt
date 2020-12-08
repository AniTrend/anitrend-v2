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
import co.anitrend.arch.extension.dispatchers.SupportDispatcher
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.arch.extension.ext.isLowRamDevice
import co.anitrend.arch.extension.ext.systemServiceOf
import co.anitrend.arch.extension.util.date.contract.ISupportDateHelper
import co.anitrend.arch.extension.util.date.SupportDateHelper
import co.anitrend.arch.ui.view.widget.model.StateLayoutConfig
import co.anitrend.core.R
import co.anitrend.core.android.controller.AndroidPowerController
import co.anitrend.core.android.controller.contract.IPowerController
import co.anitrend.core.android.shortcut.ShortcutFacade
import co.anitrend.core.android.shortcut.contract.IShortcutFacade
import co.anitrend.core.coil.fetch.RequestImageFetcher
import co.anitrend.core.coil.mapper.RequestImageMapper
import co.anitrend.core.helper.StorageHelper
import co.anitrend.core.settings.Settings
import co.anitrend.core.util.config.ConfigurationUtil
import co.anitrend.core.util.config.contract.IConfigurationUtil
import co.anitrend.core.util.locale.LocaleHelper
import co.anitrend.core.util.locale.contract.ILocaleHelper
import co.anitrend.core.util.theme.ThemeHelper
import co.anitrend.core.util.theme.contract.IThemeHelper
import co.anitrend.data.arch.di.dataModules
import co.anitrend.data.arch.network.model.NetworkMessage
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

private val coreModule = module {
    factory {
        Settings(
            context = androidContext()
        )
    } binds(Settings.BINDINGS)

    factory {
        val context = androidContext()
        NetworkMessage(
            unrecoverableErrorTittle = context.getString(R.string.error_message_unrecoverable),
            connectivityErrorTittle = context.getString(R.string.error_message_no_connection_title),
            connectivityErrorMessage = context.getString(R.string.error_message_no_connection_message)
        )
    }

    single {
        StateLayoutConfig(
            loadingDrawable = R.drawable.ic_support_empty_state,
            errorDrawable = R.drawable.ic_support_empty_state,
            loadingMessage = R.string.label_text_loading,
            retryAction = R.string.label_text_action_retry
        )
    } bind IStateLayoutConfig::class

    factory<IConfigurationUtil> {
        ConfigurationUtil(
            settings = get(),
            localeHelper = get(),
            themeHelper = get()
        )
    }

    single<ISupportDispatcher> {
        SupportDispatcher()
    }

    factory<IShortcutFacade> {
        ShortcutFacade(
            context = androidContext()
        )
    }

    factory<ISupportDateHelper> {
        SupportDateHelper()
    }

    factory<IPowerController> {
        val context = androidContext()
        AndroidPowerController(
            context = context,
            powerManager = context.systemServiceOf<PowerManager>(),
            connectivityManager = context.systemServiceOf<ConnectivityManager>(),
            connectivitySettings = get()
        )
    }

    factory {
        val localeHelper = get<ILocaleHelper>()
        PrettyTime(localeHelper.locale)
    }
}

private val configurationModule = module {
    single<ILocaleHelper> {
        LocaleHelper(
            settings = get()
        )
    }

    single<IThemeHelper> {
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
            parametersOf(HttpLoggingInterceptor.Level.BASIC)
        }.cache(
            Cache(
                StorageHelper.getImageCache(
                    context = androidContext()
                ),
                StorageHelper.getStorageUsageLimit(
                    context = androidContext(),
                    settings = get()
                )
            )
        ).build()

        val loader = ImageLoader.Builder(context)
            .availableMemoryPercentage(memoryLimit)
            .bitmapPoolPercentage(memoryLimit)
            .dispatcher(get<ISupportDispatcher>().io)
            .okHttpClient { client }
            .componentRegistry {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                    add(ImageDecoderDecoder())
                else
                    add(GifDecoder())
                add(RequestImageFetcher(client = client, mapper = get()))
                add(SvgDecoder(context = context))
                add(VideoFrameFileFetcher(context = context))
                add(VideoFrameUriFetcher(context = context))
            }

        if (!isLowRamDevice)
            loader.crossfade(
                context.resources.getInteger(
                    R.integer.motion_duration_medium
                )
            )
        else
            loader.allowRgb565(true)

        loader.build()
    }
}


internal val coreModules = listOf(
    coreModule, configurationModule
) + dataModules