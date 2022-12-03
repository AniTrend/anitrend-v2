/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.media.component.screen

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.viewbinding.ViewBinding
import co.anitrend.arch.extension.ext.extra
import co.anitrend.core.android.compose.design.ContentWrapper
import co.anitrend.core.android.ui.theme.AniTrendTheme3
import co.anitrend.core.component.screen.AniTrendScreen
import co.anitrend.media.component.compose.MediaScreenContent
import co.anitrend.media.component.viewmodel.MediaViewModel
import co.anitrend.navigation.MediaRouter
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaScreen : AniTrendScreen<ViewBinding>() {

    private val viewModel by viewModel<MediaViewModel>()

    private val mediaRouterParam: MediaRouter.Param? by extra(MediaRouter.Param.KEY)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AniTrendTheme3 {
                ContentWrapper(
                    stateFlow = viewModelState().combinedLoadState,
                    param = mediaRouterParam,
                    onLoad = viewModelState()::invoke,
                    onClick = viewModelState()::retry,
                ) {
                    MediaScreenContent(
                        mediaState = viewModelState(),
                    )
                }
            }
        }
    }

    /**
     * Proxy for a view model state if one exists
     */
    override fun viewModelState() = viewModel.state
}
