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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.anitrend.arch.extension.argument
import co.anitrend.arch.extension.getCompatColor
import co.anitrend.arch.ui.fragment.SupportFragment
import co.anitrend.onboarding.R
import co.anitrend.onboarding.model.OnBoarding
import co.anitrend.onboarding.presenter.OnBoardingPresenter
import com.airbnb.lottie.LottieDrawable
import kotlinx.android.synthetic.main.onboarding_content.*
import org.koin.android.ext.android.inject

class OnBoardingContent : SupportFragment<OnBoarding, OnBoardingPresenter, OnBoarding>() {

    private val onBoarding by argument<OnBoarding>(PARAM_KEY)

    override val inflateLayout: Int = R.layout.onboarding_content

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
     * Called immediately after [.onCreateView]
     * has returned, but before any saved state has been restored in to the view.
     * This gives subclasses a chance to initialize themselves once
     * they know their view hierarchy has been completely created.  The fragment's
     * view hierarchy is not however attached to its parent at this point.
     * @param view The View returned by [.onCreateView].
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBoarding?.apply {
            view.background = view.context.getDrawable(
                backgroundColor
            )

            lottieAnimationView.repeatCount = LottieDrawable.INFINITE
            lottieAnimationView.setAnimation(resource)

            onBoardingTitle.setTextColor(
                view.context.getCompatColor(
                    textColor
                )
            )
            onBoardingTitle.text = text
        }
    }

    override fun onResume() {
        super.onResume()
        onUpdateUserInterface()
    }

    /**
     * Handles the updating of views, binding, creation or state change, depending on the context
     * [androidx.lifecycle.LiveData] for a given [ISupportFragmentActivity] will be available by this point.
     *
     * Check implementation for more details
     */
    override fun onUpdateUserInterface() {
        lottieAnimationView.playAnimation()
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

    companion object {
        private const val PARAM_KEY = "OnBoardingContent_OnBoarding"

        fun newInstance(model: OnBoarding): OnBoardingContent {
            val fragment = OnBoardingContent()
            fragment.arguments = Bundle().apply {
                putParcelable(PARAM_KEY, model)
            }
            return fragment
        }
    }
}