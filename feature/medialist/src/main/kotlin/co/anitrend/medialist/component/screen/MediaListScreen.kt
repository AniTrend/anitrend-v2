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
package co.anitrend.medialist.component.screen

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import co.anitrend.android.core.settings.Settings
import co.anitrend.common.shared.ui.compose.DefaultScaffold
import co.anitrend.android.core.ui.theme.AniTrendTheme3
import co.anitrend.common.media.ui.controller.extensions.openMediaListSheetFor
import co.anitrend.core.component.screen.AniTrendScreen
import co.anitrend.medialist.component.compose.MediaListCompose
import co.anitrend.medialist.component.container.viewmodel.UserViewModel
import co.anitrend.medialist.component.content.viewmodel.MediaListViewModel
import co.anitrend.navigation.MediaListEditorRouter
import co.anitrend.navigation.MediaRouter
import co.anitrend.navigation.extensions.asNavPayload
import co.anitrend.navigation.extensions.startActivity
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaListScreen : AniTrendScreen() {
    private val settings by inject<Settings>()
    private val userViewModel by viewModel<UserViewModel>()
    private val mediaViewModel by viewModel<MediaListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AniTrendTheme3 {
                DefaultScaffold(onBackPress = onBackPressedDispatcher::onBackPressed) { padding ->
                    MediaListCompose(
                        settings = settings,
                        userSettings = settings,
                        userViewModel = userViewModel,
                        mediaViewModel = mediaViewModel,
                        modifier = Modifier.padding(padding),
                        onMediaItemClick = { param ->
                            when (param) {
                                is MediaRouter.MediaParam ->
                                    MediaRouter.startActivity(
                                        context = this@MediaListScreen,
                                        navPayload = param.asNavPayload(),
                                    )

                                is MediaListEditorRouter.MediaListEditorParam ->
                                    window.decorView.openMediaListSheetFor(
                                        mediaListParam = param,
                                        settings = settings,
                                    )

                                else -> Unit
                            }
                        },
                    )
                }
            }
        }
    }
}
