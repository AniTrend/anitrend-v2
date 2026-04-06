/*
 * Copyright (C) 2021 AniTrend
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
package co.anitrend.airing.component.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import co.anitrend.airing.component.compose.AiringRoute
import co.anitrend.airing.component.viewmodel.AiringViewModel
import co.anitrend.android.core.helpers.date.AniTrendDateHelper
import co.anitrend.android.core.settings.Settings
import co.anitrend.android.core.ui.theme.AniTrendTheme3
import co.anitrend.common.media.ui.controller.extensions.openMediaListSheetFor
import co.anitrend.navigation.MediaListEditorRouter
import co.anitrend.navigation.MediaRouter
import co.anitrend.navigation.extensions.asNavPayload
import co.anitrend.navigation.extensions.startActivity
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class AiringContent : Fragment() {
    private val settings by inject<Settings>()
    private val dateHelper by inject<AniTrendDateHelper>()
    private val viewModel by viewModel<AiringViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View =
        ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AniTrendTheme3 {
                    AiringRoute(
                        settings = settings,
                        userSettings = settings,
                        dateHelper = dateHelper,
                        onBackPress = requireActivity().onBackPressedDispatcher::onBackPressed,
                        onMediaItemClick = { param ->
                            when (param) {
                                is MediaRouter.MediaParam ->
                                    MediaRouter.startActivity(
                                        context = requireContext(),
                                        navPayload = param.asNavPayload(),
                                    )

                                is MediaListEditorRouter.MediaListEditorParam ->
                                    requireActivity().window.decorView.openMediaListSheetFor(param, settings)

                                else -> Unit
                            }
                        },
                        viewModel = viewModel,
                    )
                }
            }
        }
}
