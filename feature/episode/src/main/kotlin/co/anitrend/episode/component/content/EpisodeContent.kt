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
package co.anitrend.episode.component.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.anitrend.android.core.settings.common.locale.ILocaleSettings
import co.anitrend.android.core.ui.theme.AniTrendTheme3
import co.anitrend.android.core.views.compose.composable
import co.anitrend.core.component.content.compose.AniTrendComposition
import co.anitrend.core.ui.fragmentByTagOrNew
import co.anitrend.core.ui.model.FragmentItem
import co.anitrend.domain.episode.entity.Episode
import co.anitrend.episode.component.compose.EpisodeCompose
import co.anitrend.episode.component.content.viewmodel.EpisodeContentViewModel
import co.anitrend.navigation.EpisodeRouter
import co.anitrend.navigation.extensions.asBundle
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class EpisodeContent : AniTrendComposition() {
    private val settings by inject<ILocaleSettings>()
    private val viewModel by viewModel<EpisodeContentViewModel>()

    private fun openEpisodeSheet(episode: Episode) {
        val fragmentItem =
            FragmentItem(
                fragment = EpisodeRouter.forSheet(),
                parameter = EpisodeRouter.EpisodeParam(id = episode.id).asBundle(),
            )
        val dialog = fragmentItem.fragmentByTagOrNew(requireActivity())
        dialog.show(requireActivity().supportFragmentManager, fragmentItem.tag())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View =
        composable(requireActivity()) {
            AniTrendTheme3 {
                EpisodeCompose(
                    settings = settings,
                    viewModel = viewModel,
                    onEpisodeClick = ::openEpisodeSheet,
                )
            }
        }
}
