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
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.startup.AppInitializer
import co.anitrend.arch.core.model.IStateLayoutConfig
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.arch.extension.ext.getColorFromAttr
import co.anitrend.arch.extension.ext.isLowRamDevice
import co.anitrend.arch.theme.extensions.isEnvironmentNightMode
import co.anitrend.arch.ui.view.widget.model.StateLayoutConfig
import co.anitrend.core.R
import co.anitrend.core.android.koin.androidCoreModules
import co.anitrend.core.coil.client.CoilRequestClient
import co.anitrend.core.coil.fetch.RequestImageFetcher
import co.anitrend.core.coil.mapper.RequestImageMapper
import co.anitrend.data.arch.di.dataModules
import co.anitrend.data.arch.network.model.NetworkMessage
import co.anitrend.data.arch.storage.contract.IStorageController
import co.anitrend.data.arch.storage.enums.StorageType
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import coil.fetch.VideoFrameFileFetcher
import coil.fetch.VideoFrameUriFetcher
import io.wax911.emojify.initializer.EmojiInitializer
import io.wax911.emojify.manager.IEmojiManager
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.parameter.parametersOf
import org.koin.dsl.bind
import org.koin.dsl.module

private val coreModule = module {
    single {
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

    factory {
        val context = androidContext()
        CustomTabsIntent.Builder()
            .setStartAnimations(context, R.anim.enter_from_bottom, R.anim.exit_to_bottom)
            .setExitAnimations(context, android.R.anim.fade_in, android.R.anim.fade_out)
            .setColorSchemeParams(
                if (context.isEnvironmentNightMode()) CustomTabsIntent.COLOR_SCHEME_DARK
                else CustomTabsIntent.COLOR_SCHEME_LIGHT,
                CustomTabColorSchemeParams.Builder()
                    .setToolbarColor(
                        context.getColorFromAttr(R.attr.colorPrimary)
                    )
                    .setNavigationBarColor(
                        context.getColorFromAttr(R.attr.colorPrimary)
                    )
                    .setSecondaryToolbarColor(
                        context.getColorFromAttr(R.attr.colorSecondary)
                    ).build()
            )
    }
    factory<IEmojiManager> {
        AppInitializer.getInstance(androidContext())
            .initializeComponent(EmojiInitializer::class.java)
    }
}

private val configurationModule = module {
    factory {
        RequestImageMapper(
            powerController = get()
        )
    }

    factory {
        val context = androidContext()
        val isLowRamDevice = context.isLowRamDevice()
        val memoryLimit = if (isLowRamDevice) 0.15 else 0.35
        val storageController = get<IStorageController>()

        val client = get<OkHttpClient.Builder> {
            parametersOf(HttpLoggingInterceptor.Level.BASIC)
        }.cache(
            Cache(
                storageController.getImageCache(
                    context = androidContext()
                ),
                storageController.getStorageUsageLimit(
                    context = androidContext(),
                    type = StorageType.CACHE,
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
                add(
                    RequestImageFetcher(
                        client = CoilRequestClient(client),
                        mapper = get()
                    )
                )
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
) + androidCoreModules + dataModules