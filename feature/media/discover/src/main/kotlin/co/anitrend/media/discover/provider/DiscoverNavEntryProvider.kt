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
package co.anitrend.media.discover.provider

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import co.anitrend.android.core.settings.Settings
import co.anitrend.android.core.ui.theme.AniTrendTheme3
import co.anitrend.common.media.ui.controller.extensions.handleMediaItemNavigation
import co.anitrend.common.navigation.FeatureNavEntryProvider
import co.anitrend.common.navigation.FeatureNavRegistry
import co.anitrend.core.ui.fragmentByTagOrNew
import co.anitrend.core.ui.model.FragmentItem
import co.anitrend.media.discover.component.compose.MediaDiscoverCompose
import co.anitrend.media.discover.component.content.viewmodel.MediaDiscoverViewModel
import co.anitrend.navigation.MediaDiscoverFilterRouter
import co.anitrend.navigation.MediaDiscoverRouter
import co.anitrend.navigation.extensions.asBundle
import co.anitrend.navigation.extensions.fromBundle
import co.anitrend.navigation.nav3.DiscoverNavKey
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

internal class DiscoverNavEntryProvider : FeatureNavEntryProvider {
    override fun register(registry: FeatureNavRegistry) {
        registry.register(DiscoverNavKey::class) { _ ->
            DiscoverNavContent(onBackPress = ::pop)
        }
    }
}

@Composable
private fun DiscoverNavContent(onBackPress: () -> Unit) {
    val settings = koinInject<Settings>()
    val viewModel = koinViewModel<MediaDiscoverViewModel>()
    val context = LocalContext.current
    val activity = remember(context) { context as? FragmentActivity }

    if (activity != null) {
        DisposableEffect(activity, viewModel) {
            val listener =
                androidx.fragment.app.FragmentResultListener { _, bundle ->
                    val result = bundle.fromBundle<MediaDiscoverRouter.MediaDiscoverParam>()
                    result?.also(viewModel::setParam)
                }
            activity.supportFragmentManager
                .setFragmentResultListener(
                    MediaDiscoverFilterRouter.RESULT_LISTENER_KEY,
                    activity,
                    listener,
                )
            onDispose {
                activity.supportFragmentManager
                    .clearFragmentResultListener(MediaDiscoverFilterRouter.RESULT_LISTENER_KEY)
            }
        }
    }

    AniTrendTheme3 {
        MediaDiscoverCompose(
            settings = settings,
            userSettings = settings,
            onFilterClick = { currentParam ->
                val act = activity ?: return@MediaDiscoverCompose
                val fragmentItem =
                    FragmentItem(
                        fragment = MediaDiscoverFilterRouter.forSheet(),
                        parameter = currentParam.asBundle(),
                    )
                val dialog = fragmentItem.fragmentByTagOrNew(act)
                dialog.show(act.supportFragmentManager, fragmentItem.tag())
            },
            onMediaItemClick = { param ->
                activity?.handleMediaItemNavigation(param, settings)
            },
            viewModel = viewModel,
            onBackPress = onBackPress,
            showBottomBar = false,
        )
    }
}
