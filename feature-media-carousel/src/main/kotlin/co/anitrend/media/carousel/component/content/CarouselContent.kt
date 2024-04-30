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

package co.anitrend.media.carousel.component.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import co.anitrend.arch.extension.ext.argument
import co.anitrend.common.media.ui.controller.extensions.openMediaListSheetFor
import co.anitrend.core.android.compose.design.ContentWrapper
import co.anitrend.core.android.ui.theme.AniTrendTheme3
import co.anitrend.core.android.views.compose.composable
import co.anitrend.core.component.content.compose.AniTrendComposition
import co.anitrend.data.user.settings.IUserSettings
import co.anitrend.media.carousel.component.compose.CarouselScreen
import co.anitrend.media.carousel.component.content.controller.CarouselContentController
import co.anitrend.media.carousel.component.viewmodel.CarouselViewModel
import co.anitrend.navigation.AiringRouter
import co.anitrend.navigation.MediaCarouselRouter
import co.anitrend.navigation.MediaDiscoverRouter
import co.anitrend.navigation.MediaListEditorRouter
import co.anitrend.navigation.MediaRouter
import co.anitrend.navigation.extensions.asNavPayload
import co.anitrend.navigation.extensions.startActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class CarouselContent(
    private val controller: CarouselContentController,
    private val settings: IUserSettings,
) : AniTrendComposition() {

    private val viewModel by viewModel<CarouselViewModel>()
    private val param by argument<MediaCarouselRouter.MediaCarouselRouterParam>()

    private fun paramOrDefault(): MediaCarouselRouter.MediaCarouselRouterParam {
        return param ?: MediaCarouselRouter.MediaCarouselRouterParam(
            season = controller.season,
            seasonYear = controller.year,
            nextSeasonYear = controller.nextSeasonYear,
            nextSeason = controller.nextSeason,
            currentTime = controller.currentTimeAsEpoch(),
            pageSize = controller.pageSize(resources, 4),
        )
    }

    /**
     * Called to have the fragment instantiate its user interface view. This is optional, and
     * non-graphical fragments can return null. This will be called between
     * [onCreate] & [onActivityCreated].
     *
     * A default View can be returned by calling [Fragment] in your
     * constructor. Otherwise, this method returns null.
     *
     * It is recommended to __only__ inflate the layout in this method and move
     * logic that operates on the returned View to [onViewCreated].
     *
     * If you return a View from here, you will later be called in [onDestroyView]
     * when the view is being released.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment
     * @param container If non-null, this is the parent view that the fragment's UI should be
     * attached to. The fragment should not add the view itself, but this can be used to generate
     * the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return Return the View for the fragment's UI, or null.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = composable(context = inflater.context) {
        AniTrendTheme3 {
            ContentWrapper(
                stateFlow = viewModelState().combinedLoadState,
                param = paramOrDefault(),
                onLoad = viewModelState()::invoke,
                onClick = viewModelState()::retry,
            ) {
                CarouselScreen(
                    carouselState = viewModelState(),
                    mediaPreferenceData = controller.mediaPreferenceData(settings),
                    carouselItemClick = { param ->
                        when (param) {
                            is MediaDiscoverRouter.MediaDiscoverParam ->
                                MediaDiscoverRouter.startActivity(
                                    context = requireContext(),
                                    navPayload = param.asNavPayload()
                                )
                            is MediaRouter.MediaParam ->
                                MediaRouter.startActivity(
                                    context = requireContext(),
                                    navPayload = param.asNavPayload()
                                )
                            is MediaListEditorRouter.MediaListEditorParam ->
                                view?.openMediaListSheetFor(
                                    mediaListParam = param,
                                    settings = settings
                                )
                            is AiringRouter.AiringParam ->
                                AiringRouter.startActivity(
                                    context = requireContext(),
                                    navPayload = param.asNavPayload()
                                )
                            else -> Timber.e(UnsupportedOperationException("Param with type $param does not have a valid matcher"))
                        }
                    }
                )
            }
        }
    }

    /**
     * Proxy for a view model state if one exists
     */
    override fun viewModelState() = viewModel.state
}
