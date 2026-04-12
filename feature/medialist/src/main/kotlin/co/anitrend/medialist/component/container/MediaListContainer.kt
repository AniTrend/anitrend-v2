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
package co.anitrend.medialist.component.container

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.anitrend.android.core.settings.Settings
import co.anitrend.android.core.ui.theme.AniTrendTheme3
import co.anitrend.android.core.views.compose.composable
import co.anitrend.common.media.ui.controller.extensions.openMediaListSheetFor
import co.anitrend.core.component.content.compose.AniTrendComposition
import co.anitrend.medialist.component.compose.MediaListCompose
import co.anitrend.medialist.component.container.viewmodel.UserViewModel
import co.anitrend.medialist.component.content.viewmodel.MediaListViewModel
import co.anitrend.navigation.MediaListEditorRouter
import co.anitrend.navigation.MediaRouter
import co.anitrend.navigation.extensions.asNavPayload
import co.anitrend.navigation.extensions.startActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaListContainer(
    private val settings: Settings,
) : AniTrendComposition() {
    private val userViewModel by viewModel<UserViewModel>()
    private val mediaViewModel by viewModel<MediaListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View =
        composable(requireActivity()) {
            AniTrendTheme3 {
                MediaListCompose(
                    settings = settings,
                    userSettings = settings,
                    userViewModel = userViewModel,
                    mediaViewModel = mediaViewModel,
                    onMediaItemClick = { param ->
                        when (param) {
                            is MediaRouter.MediaParam ->
                                MediaRouter.startActivity(
                                    context = requireContext(),
                                    navPayload = param.asNavPayload(),
                                )

                            is MediaListEditorRouter.MediaListEditorParam ->
                                view?.openMediaListSheetFor(
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
