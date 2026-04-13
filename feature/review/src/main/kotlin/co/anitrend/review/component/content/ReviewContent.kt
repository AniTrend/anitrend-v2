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
package co.anitrend.review.component.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.anitrend.android.core.ui.theme.AniTrendTheme3
import co.anitrend.android.core.views.compose.composable
import co.anitrend.core.component.content.compose.AniTrendComposition
import co.anitrend.review.component.compose.ReviewRoute
import co.anitrend.review.component.viewmodel.ReviewViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReviewContent : AniTrendComposition() {
    private val viewModel by viewModel<ReviewViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View =
        composable(requireActivity()) {
            AniTrendTheme3 {
                ReviewRoute(
                    viewModel = viewModel,
                    onBackPress = requireActivity().onBackPressedDispatcher::onBackPressed,
                    showBottomBar = false,
                )
            }
        }
}
