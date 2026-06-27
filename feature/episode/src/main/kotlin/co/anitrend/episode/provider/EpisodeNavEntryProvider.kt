/*
 * Copyright (C) 2025 AniTrend
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
package co.anitrend.episode.provider

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import co.anitrend.android.core.settings.common.locale.ILocaleSettings
import co.anitrend.android.core.ui.theme.AniTrendTheme3
import co.anitrend.common.navigation.FeatureNavEntryProvider
import co.anitrend.common.navigation.FeatureNavRegistry
import co.anitrend.core.ui.fragmentByTagOrNew
import co.anitrend.core.ui.model.FragmentItem
import co.anitrend.domain.episode.entity.Episode
import co.anitrend.episode.component.compose.EpisodeCompose
import co.anitrend.episode.component.content.viewmodel.EpisodeContentViewModel
import co.anitrend.navigation.EpisodeRouter
import co.anitrend.navigation.extensions.asBundle
import co.anitrend.navigation.nav3.EpisodesNavKey
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

internal class EpisodeNavEntryProvider : FeatureNavEntryProvider {
    override fun register(registry: FeatureNavRegistry) {
        registry.register(EpisodesNavKey::class) { _ ->
            EpisodeNavContent(onBackPress = ::pop)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EpisodeNavContent(onBackPress: () -> Unit) {
    val settings = koinInject<ILocaleSettings>()
    val viewModel = koinViewModel<EpisodeContentViewModel>()
    val context = LocalContext.current

    AniTrendTheme3 {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Episodes") },
                    navigationIcon = {
                        IconButton(onClick = onBackPress) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                            )
                        }
                    },
                )
            },
        ) { paddingValues ->
            Box(Modifier.fillMaxSize().padding(paddingValues)) {
                EpisodeCompose(
                    settings = settings,
                    viewModel = viewModel,
                    onEpisodeClick = { episode ->
                        val activity = context as? FragmentActivity ?: return@EpisodeCompose
                        val fragmentItem =
                            FragmentItem(
                                fragment = EpisodeRouter.forSheet(),
                                parameter = EpisodeRouter.EpisodeParam(id = episode.id).asBundle(),
                            )
                        val dialog = fragmentItem.fragmentByTagOrNew(activity)
                        dialog.show(activity.supportFragmentManager, fragmentItem.tag())
                    },
                )
            }
        }
    }
}
