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

import android.os.Build
import co.anitrend.arch.extension.dispatchers.SupportDispatchers
import co.anitrend.arch.extension.ext.isLowRamDevice
import co.anitrend.arch.ui.view.widget.model.StateLayoutConfig
import co.anitrend.core.R
import co.anitrend.core.helper.StorageHelper
import co.anitrend.core.presenter.CorePresenter
import co.anitrend.core.settings.Settings
import co.anitrend.core.settings.common.cache.ICacheSettings
import co.anitrend.core.util.config.ConfigurationUtil
import co.anitrend.core.util.locale.LocaleHelper
import co.anitrend.core.util.theme.ThemeHelper
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import coil.fetch.VideoFrameFileFetcher
import coil.fetch.VideoFrameUriFetcher
import okhttp3.Cache
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.binds
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

private val coreModule = module {
    factory {
        Settings(
            context = androidContext()
        )
    } binds(Settings.BINDINGS)
    single {
        StateLayoutConfig(
            loadingDrawable = R.drawable.ic_anitrend_notification_logo,
            errorDrawable = R.drawable.ic_support_empty_state,
            loadingMessage = R.string.label_text_loading,
            retryAction = R.string.label_text_action_retry
        )
    }
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
        val isLowRamDevice = androidContext().isLowRamDevice()
        val memoryLimit = if (isLowRamDevice) 0.15 else 0.35
        val dispatchers = get<SupportDispatchers>()
        val context = androidContext()
        val loader = ImageLoader.Builder(context)
            .availableMemoryPercentage(memoryLimit)
            .bitmapPoolPercentage(memoryLimit)
            .dispatcher(dispatchers.io)
            .okHttpClient {
                OkHttpClient.Builder().cache(
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
                )
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build()
            }.componentRegistry {
                if (Build.VERSION.SDK_INT >= 28)
                    add(ImageDecoderDecoder())
                else
                    add(GifDecoder())
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


val coreModules = listOf(
    coreModule, configurationModule
)