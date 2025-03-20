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
package co.anitrend.onboarding.component.screen

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import co.anitrend.arch.extension.ext.hideStatusBarAndNavigationBar
import co.anitrend.core.android.ui.theme.AniTrendTheme3
import co.anitrend.core.component.screen.AniTrendScreen
import co.anitrend.core.ui.inject
import co.anitrend.onboarding.component.compose.OnBoardingScreenContent
import co.anitrend.onboarding.component.presenter.OnBoardingPresenter

class OnBoardingScreen : AniTrendScreen() {
    private val presenter by inject<OnBoardingPresenter>()

    override fun configureActivity() {
        super.configureActivity()
        hideStatusBarAndNavigationBar()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AniTrendTheme3 {
                Surface {
                    OnBoardingScreenContent(
                        onBoardingPages = presenter.onBoardingPages,
                        onBoardingCompleted = {
                            presenter.onBoardingExperienceCompleted(onActionCompleted = ::finish)
                        },
                    )
                }
            }
        }
    }
}
