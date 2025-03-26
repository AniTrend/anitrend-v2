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
package co.anitrend.about.component.screen

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.lifecycle.liveData
import co.anitrend.about.component.compose.AboutScreenContent
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.android.core.compose.design.ContentWrapper
import co.anitrend.android.core.ui.theme.AniTrendTheme3
import co.anitrend.core.component.screen.AniTrendScreen
import co.anitrend.navigation.model.common.IParam

class AboutScreen : AniTrendScreen() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AniTrendTheme3 {
                ContentWrapper(
                    stateFlow = liveData { emit(LoadState.Idle()) },
                    param = IParam.None,
                ) {
                    AboutScreenContent(
                        onBackPress = onBackPressedDispatcher::onBackPressed,
                    )
                }
            }
        }
    }
}
