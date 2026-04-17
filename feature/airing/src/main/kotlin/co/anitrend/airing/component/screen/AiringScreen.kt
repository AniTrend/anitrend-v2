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
package co.anitrend.airing.component.screen

import android.os.Bundle
import androidx.activity.compose.setContent
import co.anitrend.airing.component.compose.AiringRoute
import co.anitrend.android.core.helpers.date.AniTrendDateHelper
import co.anitrend.android.core.settings.Settings
import co.anitrend.android.core.ui.theme.AniTrendTheme3
import co.anitrend.common.media.ui.controller.extensions.handleMediaItemNavigation
import co.anitrend.core.component.screen.AniTrendScreen
import co.anitrend.core.ui.inject

class AiringScreen : AniTrendScreen() {
    private val settings by inject<Settings>()
    private val dateHelper by inject<AniTrendDateHelper>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AniTrendTheme3 {
                AiringRoute(
                    settings = settings,
                    userSettings = settings,
                    dateHelper = dateHelper,
                    onBackPress = onBackPressedDispatcher::onBackPressed,
                    onMediaItemClick = { param -> handleMediaItemNavigation(param, settings) },
                )
            }
        }
    }
}
