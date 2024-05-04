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
import co.anitrend.core.android.storage.contract.IStorageController
import co.anitrend.core.android.storage.enums.StorageType
import co.anitrend.core.coil.client.CoilRequestClient
import co.anitrend.core.coil.fetch.RequestImageFetcher
import co.anitrend.core.coil.mapper.RequestImageMapper
import co.anitrend.data.android.koin.dataModules
import co.anitrend.data.android.network.model.NetworkMessage
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import coil.decode.VideoFrameDecoder
import coil.disk.DiskCache
import coil.memory.MemoryCache
import io.wax911.emojify.EmojiManager
import io.wax911.emojify.serializer.kotlinx.KotlinxDeserializer
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
            loadingDrawable = co.anitrend.arch.ui.R.drawable.ic_support_empty_state,
            errorDrawable = co.anitrend.arch.ui.R.drawable.ic_support_empty_state,
            loadingMessage = R.string.label_text_loading,
            defaultMessage = R.string.label_text_end_of_list,
            retryAction = R.string.label_text_action_retry
        )
    } bind IStateLayoutConfig::class

    factory {
        val context = androidContext()
        CustomTabsIntent.Builder()
            .setStartAnimations(context, co.anitrend.core.android.R.anim.enter_from_bottom, co.anitrend.core.android.R.anim.exit_to_bottom)
            .setExitAnimations(context, android.R.anim.fade_in, android.R.anim.fade_out)
            .setColorSchemeParams(
                if (context.isEnvironmentNightMode()) CustomTabsIntent.COLOR_SCHEME_DARK
                else CustomTabsIntent.COLOR_SCHEME_LIGHT,
                CustomTabColorSchemeParams.Builder()
                    .setToolbarColor(
                        context.getColorFromAttr(androidx.appcompat.R.attr.colorPrimary)
                    )
                    .setNavigationBarColor(
                        context.getColorFromAttr(androidx.appcompat.R.attr.colorPrimary)
                    )
                    .setSecondaryToolbarColor(
                        context.getColorFromAttr(com.google.android.material.R.attr.colorSecondary)
                    ).build()
            )
    }
    factory {
        EmojiManager.create(
            context = androidContext(),
            serializer = KotlinxDeserializer()
        )
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
        }.build()

        val imageCache = storageController.getImageCache(
            context = androidContext()
        )

        val loader = ImageLoader.Builder(context)
            .memoryCache {
                MemoryCache.Builder(context)
                    .maxSizePercent(memoryLimit)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(imageCache)
                    .maxSizeBytes(
                        storageController.getStorageUsageLimit(
                            context = androidContext(),
                            type = StorageType.CACHE,
                            settings = get()
                        )
                    ).build()
            }
            .dispatcher(get<ISupportDispatcher>().io)
            .okHttpClient { client }
            .components {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                    add(ImageDecoderDecoder.Factory())
                else
                    add(GifDecoder.Factory())
                add(
                    RequestImageFetcher.Factory(
                        client = CoilRequestClient(client, imageCache),
                        mapper = get()
                    )
                )
                add(SvgDecoder.Factory())
                add(VideoFrameDecoder.Factory())
            }

        if (!isLowRamDevice)
            loader.crossfade(
                context.resources.getInteger(
                    co.anitrend.core.android.R.integer.motion_duration_medium
                )
            )
        else
            loader.allowRgb565(true)

        loader.build()
    }
}


internal val coreModules = module {
    includes(coreModule, configurationModule, androidCoreModules, dataModules)
}
