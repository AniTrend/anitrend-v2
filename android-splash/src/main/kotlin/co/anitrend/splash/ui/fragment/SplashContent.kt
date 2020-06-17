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

package co.anitrend.splash.ui.fragment

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import co.anitrend.core.extensions.injectScoped
import co.anitrend.core.ui.fragment.AniTrendFragment
import co.anitrend.splash.R
import co.anitrend.splash.koin.moduleHelper
import co.anitrend.splash.presenter.SplashPresenter

class SplashContent(
    override val inflateLayout: Int = R.layout.content_splash
) : AniTrendFragment() {

    /**
     * Invoke view model observer to watch for changes, this will be called
     * called in [onViewCreated]
     */
    override fun setUpViewModelObserver() {

    }

    private val presenter by injectScoped<SplashPresenter>()

    /**
     * Additional initialization to be done in this method, this method will be called in
     * [androidx.fragment.app.FragmentActivity.onCreate].
     *
     * @param savedInstanceState
     */
    override fun initializeComponents(savedInstanceState: Bundle?) {
        lifecycleScope.launchWhenResumed {
            presenter.firstRunCheck(activity)
        }
    }

    /**
     * Expects a module helper if one is available for the current scope, otherwise return null
     */
    override fun featureModuleHelper() = moduleHelper
}