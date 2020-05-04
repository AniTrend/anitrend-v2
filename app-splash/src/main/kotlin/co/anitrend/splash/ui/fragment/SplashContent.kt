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

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import co.anitrend.arch.extension.attachComponent
import co.anitrend.arch.extension.detachComponent
import co.anitrend.core.ui.fragment.AniTrendFragment
import co.anitrend.core.ui.fragment.contract.IFragmentFactory
import co.anitrend.splash.R
import co.anitrend.splash.presenter.SplashPresenter
import co.anitrend.core.extensions.injectScoped
import co.anitrend.splash.koin.moduleHelper

class SplashContent(
    override val inflateLayout: Int = R.layout.content_splash
) : AniTrendFragment<Nothing>() {

    private val presenter by injectScoped<SplashPresenter>()

    /**
     * Called when a fragment is first attached to its context.
     * [onCreate] will be called after this.
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        attachComponent(moduleHelper)
    }

    /**
     * Invoke view model observer to watch for changes
     */
    override fun setUpViewModelObserver() {

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
            onFetchDataInitialize()
        }
    }

    /**
     * Handles the updating, binding, creation or state change, depending on the context of views.
     *
     * **N.B.** Where this is called is up to the developer
     */
    override fun onUpdateUserInterface() {

    }

    /**
     * Handles the complex logic required to dispatch network request to a view model, presenter or
     * worker to either request from the network or database cache.
     *
     * **N.B.** Where this is called is up to the developer
     */
    override fun onFetchDataInitialize() {
        // some state checks and finally
        onUpdateUserInterface()
    }

    /**
     * Called when the fragment is no longer attached to its activity.  This
     * is called after [onDestroy].
     */
    override fun onDetach() {
        detachComponent(moduleHelper)
        super.onDetach()
    }

    companion object : IFragmentFactory<SplashContent> {
        override val fragmentTag = SplashContent::class.java.simpleName

        override fun newInstance(bundle: Bundle?) = SplashContent()
    }
}