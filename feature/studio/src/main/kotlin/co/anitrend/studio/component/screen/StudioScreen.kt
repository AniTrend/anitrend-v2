/*
 * Copyright (C) 2020 AniTrend
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
package co.anitrend.studio.component.screen

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import co.anitrend.android.core.ui.theme.AniTrendTheme3
import co.anitrend.arch.extension.ext.extra
import co.anitrend.core.component.screen.AniTrendScreen
import co.anitrend.navigation.StudioRouter
import co.anitrend.navigation.extensions.nameOf
import co.anitrend.studio.component.compose.StudioScreenContent
import co.anitrend.studio.component.viewmodel.StudioViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class StudioScreen : AniTrendScreen() {
    private val viewModel by viewModel<StudioViewModel>()

    private val param by extra<StudioRouter.StudioParam>(
        key = nameOf<StudioRouter.StudioParam>(),
    )

    override fun initializeComponents(savedInstanceState: Bundle?) {
        param?.let(viewModel::invoke)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AniTrendTheme3 {
                val state by viewModel.model.observeAsState()
                val loadState by viewModel.loadState.observeAsState()
                StudioScreenContent(
                    state = state,
                    loadState = loadState,
                    onBackPress = onBackPressedDispatcher::onBackPressed,
                    onRetry = viewModel::retryCurrent,
                )
            }
        }
    }
}
