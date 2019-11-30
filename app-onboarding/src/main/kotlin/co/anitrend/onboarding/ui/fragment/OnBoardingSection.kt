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

package co.anitrend.onboarding.ui.fragment

import android.os.Bundle
import co.anitrend.arch.ui.fragment.SupportFragment
import co.anitrend.onboarding.model.OnBoarding
import co.anitrend.onboarding.presenter.OnBoardingPresenter
import org.koin.android.ext.android.inject

class OnBoardingSection : SupportFragment<OnBoarding, OnBoardingPresenter, OnBoarding>() {

    /**
     * Invoke view model observer to watch for changes
     */
    override fun setUpViewModelObserver() {

    }

    /**
     * Should be created lazily through injection or lazy delegate
     *
     * @return supportPresenter of the generic type specified
     */
    override val supportPresenter by inject<OnBoardingPresenter>()

    /**
     * Additional initialization to be done in this method, if the overriding class is type of
     * [androidx.fragment.app.Fragment] then this method will be called in
     * [androidx.fragment.app.FragmentActivity.onCreate]. Otherwise
     * [androidx.fragment.app.FragmentActivity.onPostCreate] invokes this function
     *
     * @param savedInstanceState
     */
    override fun initializeComponents(savedInstanceState: Bundle?) {

    }

    /**
     * Handles the updating of views, binding, creation or state change, depending on the context
     * [androidx.lifecycle.LiveData] for a given [ISupportFragmentActivity] will be available by this point.
     *
     * Check implementation for more details
     */
    override fun onUpdateUserInterface() {

    }

    /**
     * Handles the complex logic required to dispatch network request to [ISupportViewModel]
     * to either request from the network or database cache.
     *
     * The results of the dispatched network or cache call will be published by the
     * [androidx.lifecycle.LiveData] specifically [ISupportViewModel.model]
     *
     * @see [ISupportViewModel.invoke]
     */
    override fun onFetchDataInitialize() {

    }
}