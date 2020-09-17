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

package co.anitrend.splash.ui.activity

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import co.anitrend.arch.extension.ext.UNSAFE
import co.anitrend.core.ui.commit
import co.anitrend.core.extensions.hideStatusBarAndNavigationBar
import co.anitrend.core.ui.activity.AnitrendActivity
import co.anitrend.core.ui.fragment.model.FragmentItem
import co.anitrend.splash.databinding.ActivitySplashBinding
import co.anitrend.splash.ui.fragment.SplashContent
import kotlinx.coroutines.launch

class SplashScreen : AnitrendActivity() {

    private val binding by lazy(UNSAFE) {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun configureActivity() {
        super.configureActivity()
        hideStatusBarAndNavigationBar()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun initializeComponents(savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            onUpdateUserInterface()
        }
    }

    private fun onUpdateUserInterface() {
        currentFragmentTag = FragmentItem(fragment = SplashContent::class.java)
                .commit(binding.splashFrame, this) {}
    }
}
