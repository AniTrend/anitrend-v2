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

package co.anitrend.splash.component.content

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import co.anitrend.core.component.content.AniTrendContent
import co.anitrend.core.ui.inject
import co.anitrend.splash.R
import co.anitrend.splash.component.presenter.SplashPresenter
import co.anitrend.splash.databinding.ContentSplashBinding

class SplashContent(
    override val inflateLayout: Int = R.layout.content_splash
) : AniTrendContent<ContentSplashBinding>() {

    /**
     * Invoke view model observer to watch for changes, this will be called
     * called in [onViewCreated]
     */
    override fun setUpViewModelObserver() {

    }

    private val presenter by inject<SplashPresenter>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ContentSplashBinding.bind(view)
    }

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
}