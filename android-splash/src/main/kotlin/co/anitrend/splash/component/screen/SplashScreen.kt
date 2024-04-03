/*
 * Copyright (C) 2019  AniTrend
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
package co.anitrend.splash.component.screen

import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import co.anitrend.arch.extension.ext.hideStatusBarAndNavigationBar
import co.anitrend.core.component.screen.AniTrendBoundScreen
import co.anitrend.core.ui.commit
import co.anitrend.core.ui.model.FragmentItem
import co.anitrend.splash.component.content.SplashContent
import co.anitrend.splash.databinding.ActivitySplashBinding

class SplashScreen : AniTrendBoundScreen<ActivitySplashBinding>() {
    override fun configureActivity() {
        super.configureActivity()
        hideStatusBarAndNavigationBar()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(requireBinding().root)
        with(splashScreen) {
            setKeepOnScreenCondition {
                onUpdateUserInterface()
                false
            }

            setOnExitAnimationListener { splashScreenView ->
                splashScreenView.remove()
            }
        }
    }

    override fun initializeComponents(savedInstanceState: Bundle?) {
    }

    private fun onUpdateUserInterface() {
        currentFragmentTag =
            FragmentItem(fragment = SplashContent::class.java)
                .commit(requireBinding().authFrame, this)
    }
}
