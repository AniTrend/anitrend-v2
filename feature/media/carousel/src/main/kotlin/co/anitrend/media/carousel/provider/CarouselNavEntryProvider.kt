/*
 * Copyright (C) 2025 AniTrend
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
package co.anitrend.media.carousel.provider

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import co.anitrend.android.core.compose.design.ContentWrapper
import co.anitrend.android.core.ui.theme.AniTrendTheme3
import co.anitrend.common.media.ui.controller.extensions.openMediaListSheetFor
import co.anitrend.common.navigation.FeatureNavEntryProvider
import co.anitrend.common.navigation.FeatureNavRegistry
import co.anitrend.data.user.settings.IUserSettings
import co.anitrend.media.carousel.component.compose.CarouselScreenContent
import co.anitrend.media.carousel.component.content.controller.CarouselContentController
import co.anitrend.media.carousel.component.viewmodel.CarouselViewModel
import co.anitrend.navigation.AiringRouter
import co.anitrend.navigation.MediaCarouselRouter
import co.anitrend.navigation.MediaDiscoverRouter
import co.anitrend.navigation.MediaListEditorRouter
import co.anitrend.navigation.MediaRouter
import co.anitrend.navigation.extensions.asNavPayload
import co.anitrend.navigation.extensions.startActivity
import co.anitrend.navigation.nav3.HomeNavKey
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import timber.log.Timber

internal class CarouselNavEntryProvider : FeatureNavEntryProvider {
    override fun register(registry: FeatureNavRegistry) {
        registry.register(HomeNavKey::class) { _ ->
            CarouselNavContent(onBackPress = ::pop)
        }
    }
}

@Composable
private fun CarouselNavContent(onBackPress: () -> Unit) {
    val settings = koinInject<IUserSettings>()
    val controller = koinInject<CarouselContentController>()
    val viewModel = koinViewModel<CarouselViewModel>()
    val context = LocalContext.current

    val param =
        remember(controller) {
            MediaCarouselRouter.MediaCarouselRouterParam(
                season = controller.season,
                seasonYear = controller.year,
                nextSeasonYear = controller.nextSeasonYear,
                nextSeason = controller.nextSeason,
                currentTime = controller.currentTimeAsEpoch(),
                pageSize = controller.pageSize(context.resources, 4),
            )
        }

    AniTrendTheme3 {
        ContentWrapper(
            stateFlow = viewModel.loadState,
            param = param,
            onLoad = viewModel::invoke,
            onClick = viewModel::retry,
        ) {
            CarouselScreenContent(
                data = viewModel.model,
                mediaPreferenceData = controller.mediaPreferenceData(settings),
                carouselItemClick = { param ->
                    when (param) {
                        is MediaDiscoverRouter.MediaDiscoverParam ->
                            MediaDiscoverRouter.startActivity(
                                context = context,
                                navPayload = param.asNavPayload(),
                            )
                        is MediaRouter.MediaParam ->
                            MediaRouter.startActivity(
                                context = context,
                                navPayload = param.asNavPayload(),
                            )
                        is MediaListEditorRouter.MediaListEditorParam -> {
                            val activity = context as? FragmentActivity ?: return@CarouselScreenContent
                            activity.window.decorView.openMediaListSheetFor(
                                mediaListParam = param,
                                settings = settings,
                            )
                        }
                        is AiringRouter.AiringParam ->
                            AiringRouter.startActivity(
                                context = context,
                                navPayload = param.asNavPayload(),
                            )
                        else -> Timber.e("Unmatched carousel item click: $param")
                    }
                },
            )
        }
    }
}
