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
import co.anitrend.android.core.settings.Settings
import co.anitrend.android.core.ui.theme.AniTrendTheme3
import co.anitrend.common.media.ui.controller.extensions.openMediaListSheetFor
import co.anitrend.core.component.screen.AniTrendScreen
import co.anitrend.core.ui.fragmentByTagOrNew
import co.anitrend.core.ui.model.FragmentItem
import co.anitrend.data.settings.customize.common.PreferredViewMode
import co.anitrend.media.discover.component.compose.MediaDiscoverCompose
import co.anitrend.media.discover.component.content.viewmodel.MediaDiscoverViewModel
import co.anitrend.navigation.MediaDiscoverFilterRouter
import co.anitrend.navigation.MediaDiscoverRouter
import co.anitrend.navigation.MediaListEditorRouter
import co.anitrend.navigation.MediaRouter
import co.anitrend.navigation.extensions.asBundle
import co.anitrend.navigation.extensions.asNavPayload
import co.anitrend.navigation.extensions.fromBundle
import co.anitrend.navigation.extensions.startActivity
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaDiscoverScreen : AniTrendScreen() {
    private val viewModel by viewModel<MediaDiscoverViewModel>()
    private val settings by inject<Settings>()

    private fun openMediaFilterDialog(param: MediaDiscoverRouter.MediaDiscoverParam) {
        val fragmentItem =
            FragmentItem(
                fragment = MediaDiscoverFilterRouter.forSheet(),
                parameter = param.asBundle(),
            )
        val dialog = fragmentItem.fragmentByTagOrNew(this)
        dialog.show(supportFragmentManager, fragmentItem.tag())
    }

    private fun cycleViewMode() {
        val entries = PreferredViewMode.entries
        val currentIndex = entries.indexOf(settings.preferredViewMode.value)
        settings.preferredViewMode.value = entries[(currentIndex + 1) % entries.size]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.setFragmentResultListener(
            MediaDiscoverFilterRouter.RESULT_LISTENER_KEY,
            this,
        ) { _, bundle ->
            val result = bundle.fromBundle<MediaDiscoverRouter.MediaDiscoverParam>()
            result?.also(viewModel::setParam)
        }
        setContent {
            AniTrendTheme3 {
                MediaDiscoverCompose(
                    settings = settings,
                    userSettings = settings,
                    onBackPress = onBackPressedDispatcher::onBackPressed,
                    onFilterClick = ::openMediaFilterDialog,
                    onViewModeClick = ::cycleViewMode,
                    onMediaItemClick = { param ->
                        when (param) {
                            is MediaRouter.MediaParam ->
                                MediaRouter.startActivity(
                                    context = this@MediaDiscoverScreen,
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
                    viewModel = viewModel,
                )
            }
        }
    }
}
