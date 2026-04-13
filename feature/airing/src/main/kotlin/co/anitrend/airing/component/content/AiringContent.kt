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
import co.anitrend.airing.component.compose.AiringRoute
import co.anitrend.airing.component.viewmodel.AiringViewModel
import co.anitrend.android.core.helpers.date.AniTrendDateHelper
import co.anitrend.android.core.settings.Settings
import co.anitrend.android.core.ui.theme.AniTrendTheme3
import co.anitrend.android.core.views.compose.composable
import co.anitrend.common.media.ui.controller.extensions.handleMediaItemNavigation
import co.anitrend.core.component.content.compose.AniTrendComposition
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class AiringContent : AniTrendComposition() {
    private val settings by inject<Settings>()
    private val dateHelper by inject<AniTrendDateHelper>()
    private val viewModel by viewModel<AiringViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View =
        composable(requireActivity()) {
            AniTrendTheme3 {
                AiringRoute(
                    settings = settings,
                    userSettings = settings,
                    dateHelper = dateHelper,
                    onBackPress = requireActivity().onBackPressedDispatcher::onBackPressed,
                    onMediaItemClick = { param -> requireActivity().handleMediaItemNavigation(param, settings) },
                    viewModel = viewModel,
                )
            }
        }
}
