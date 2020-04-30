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
import androidx.fragment.app.commit
import co.anitrend.arch.extension.LAZY_MODE_UNSAFE
import co.anitrend.arch.ui.activity.SupportActivity
import co.anitrend.core.extensions.hideStatusBarAndNavigationBar
import co.anitrend.core.ui.activity.AnitrendActivity
import co.anitrend.splash.R
import co.anitrend.splash.databinding.ActivitySplashBinding
import co.anitrend.splash.koin.injectFeatureModules
import co.anitrend.splash.ui.fragment.SplashContent
import co.anitrend.splash.presenter.SplashPresenter
import org.koin.android.ext.android.inject

class SplashScreen : AnitrendActivity<Nothing, SplashPresenter>() {

    private val binding by lazy(LAZY_MODE_UNSAFE) {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    /**
     * Should be created lazily through injection or lazy delegate
     *
     * @return supportPresenter of the generic type specified
     */
    override val supportPresenter by inject<SplashPresenter>()

    /**
     * Can be used to configure custom theme styling as desired
     */
    override fun configureActivity() {
        super.configureActivity()
        hideStatusBarAndNavigationBar()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    /**
     * Additional initialization to be done in this method, if the overriding class is type of [SupportFragment]
     * then this method will be called in [SupportFragment.onCreate]. Otherwise [SupportActivity.onPostCreate]
     * invokes this function
     *
     * @see [SupportActivity.onPostCreate] and [SupportFragment.onCreate]
     * @param
     */
    override fun initializeComponents(savedInstanceState: Bundle?) {
        injectFeatureModules()
        onUpdateUserInterface()
    }

    /**
     * Handles the updating of views, binding, creation or state change, depending on the context
     * [androidx.lifecycle.LiveData] for a given [ISupportFragmentActivity] will be available by this point.
     *
     * Check implementation for more details
     */
    override fun onUpdateUserInterface() {
        val fragment = supportFragmentManager.findFragmentByTag(
            SplashContent.FRAGMENT_TAG
        ) ?: SplashContent.newInstance()

        supportFragmentManager.commit {
            replace(
                binding.splashFrame.id,
                fragment,
                SplashContent.FRAGMENT_TAG
            )
        }
    }
}
