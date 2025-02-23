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
package co.anitrend.profile.component.screen

import android.os.Bundle
import androidx.activity.compose.setContent
import co.anitrend.arch.extension.ext.extra
import co.anitrend.common.shared.ui.extension.shareContent
import co.anitrend.core.android.compose.design.ContentWrapper
import co.anitrend.core.android.ui.theme.AniTrendTheme3
import co.anitrend.core.component.screen.AniTrendScreen
import co.anitrend.navigation.ImageViewerRouter
import co.anitrend.navigation.NotificationRouter
import co.anitrend.navigation.ProfileRouter
import co.anitrend.navigation.extensions.asNavPayload
import co.anitrend.navigation.extensions.nameOf
import co.anitrend.navigation.extensions.startActivity
import co.anitrend.profile.component.compose.ProfileScreenContent
import co.anitrend.profile.component.viewmodel.ProfileViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileScreen : AniTrendScreen() {
    private val viewModel by viewModel<ProfileViewModel>()
    private val param by extra<ProfileRouter.ProfileParam>(
        key = nameOf<ProfileRouter.ProfileParam>(),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AniTrendTheme3 {
                ContentWrapper(
                    stateFlow = viewModel.loadState,
                    param = param,
                    onLoad = viewModel::invoke,
                    onClick = viewModel::retry,
                ) {
                    ProfileScreenContent(
                        viewModel = viewModelState(),
                        onImageClick = { param ->
                            ImageViewerRouter.startActivity(
                                context = this@ProfileScreen,
                                navPayload = param.asNavPayload(),
                            )
                        },
                        onFloatingActionButtonClick = { url ->
                            shareContent {
                                setType("text/plain")
                                setText(url)
                            }
                        },
                        onInboxButtonClick = {
                            // TODO: Create an inbox or messages router
                        },
                        onNotificationsButtonClick = {
                            NotificationRouter.startActivity(this)
                        },
                        onBackClick = onBackPressedDispatcher::onBackPressed,
                    )
                }
            }
        }
    }

    /**
     * Proxy for a view model state if one exists
     */
    override fun viewModelState() = viewModel
}
