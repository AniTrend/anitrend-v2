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
package co.anitrend.media.discover.component.screen

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import co.anitrend.common.shared.ui.compose.FragmentItemHost
import co.anitrend.android.core.compose.design.BackIconButton
import co.anitrend.android.core.ui.theme.AniTrendTheme3
import co.anitrend.core.component.screen.AniTrendScreen
import co.anitrend.core.ui.fragmentByTagOrNew
import co.anitrend.core.ui.inject
import co.anitrend.core.ui.model.FragmentItem
import co.anitrend.data.settings.customize.ICustomizationSettings
import co.anitrend.data.settings.customize.common.PreferredViewMode
import co.anitrend.media.discover.component.content.viewmodel.MediaDiscoverViewModel
import co.anitrend.navigation.MediaDiscoverFilterRouter
import co.anitrend.navigation.MediaDiscoverRouter
import co.anitrend.navigation.extensions.asBundle
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaDiscoverScreen : AniTrendScreen() {
    private val viewModel by viewModel<MediaDiscoverViewModel>()
    private val settings by inject<ICustomizationSettings>()

    private fun openMediaFilterDialog() {
        val fragmentItem =
            FragmentItem(
                fragment = MediaDiscoverFilterRouter.forSheet(),
                parameter = viewModel.getParam().asBundle(),
            )
        val dialog = fragmentItem.fragmentByTagOrNew(this)
        dialog.show(supportFragmentManager, fragmentItem.tag())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AniTrendTheme3 {
                MediaDiscoverCompose(
                    onBackPress = onBackPressedDispatcher::onBackPressed,
                    onFilterClick = ::openMediaFilterDialog,
                    onViewModeClick = {
                        val entries = PreferredViewMode.entries
                        val currentIndex = entries.indexOf(settings.preferredViewMode.value)
                        settings.preferredViewMode.value = entries[(currentIndex + 1) % entries.size]
                    },
                    bundle = intent.extras,
                )
            }
        }
    }
}

@Composable
internal fun MediaDiscoverCompose(
    onBackPress: () -> Unit,
    onFilterClick: () -> Unit,
    onViewModeClick: () -> Unit,
    bundle: Bundle?,
) {
    Scaffold(
        bottomBar = {
            BottomAppBar(
                actions = {
                    BackIconButton(onBackClick = onBackPress)
                    IconButton(onClick = onFilterClick) {
                        Icon(imageVector = Icons.Default.FilterList, contentDescription = "Filter")
                    }
                    IconButton(onClick = onViewModeClick) {
                        Icon(imageVector = Icons.Default.GridView, contentDescription = "View mode")
                    }
                },
            )
        },
    ) {
        FragmentItemHost(
            modifier = Modifier.padding(it),
            fragmentItem =
                FragmentItem(
                    fragment = MediaDiscoverRouter.forFragment(),
                    parameter = bundle,
                ),
        )
    }
}
