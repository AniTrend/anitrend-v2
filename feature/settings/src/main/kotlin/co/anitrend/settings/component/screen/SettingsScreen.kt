/*
 * Copyright (C) 2019 AniTrend
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
package co.anitrend.settings.component.screen

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.liveData
import androidx.navigation.compose.rememberNavController
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.android.core.compose.design.ContentWrapper
import co.anitrend.android.core.ui.theme.AniTrendTheme3
import co.anitrend.core.component.screen.AniTrendScreen
import co.anitrend.core.ui.inject
import co.anitrend.navigation.model.common.IParam
import co.anitrend.settings.component.compose.SettingsContentScreen
import co.anitrend.settings.component.presenter.SettingsPresenter

class SettingsScreen : AniTrendScreen() {
    private val presenter by inject<SettingsPresenter>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AniTrendTheme3 {
                ContentWrapper(
                    stateFlow = liveData { emit(LoadState.Idle()) },
                    param = IParam.None,
                ) {
                    val navController = rememberNavController()
                    val settingItems by remember {
                        derivedStateOf {
                            presenter.getSettingsItems(
                                navigateTo = {
                                    navController.navigate(route = it.name)
                                },
                            )
                        }
                    }
                    SettingsContentScreen(
                        navigationController = navController,
                        settingsItems = settingItems,
                        onBackPress = {
                            if (!navController.popBackStack()) {
                                onBackPressedDispatcher.onBackPressed()
                            }
                        },
                    )
                }
            }
        }
    }
}
