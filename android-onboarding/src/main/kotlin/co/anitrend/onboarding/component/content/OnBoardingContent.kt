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

package co.anitrend.onboarding.component.content

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import co.anitrend.arch.extension.ext.argument
import co.anitrend.arch.extension.ext.getCompatDrawable
import co.anitrend.core.component.content.AniTrendContent
import co.anitrend.onboarding.R
import co.anitrend.onboarding.databinding.OnboardingContentBinding
import co.anitrend.onboarding.model.OnBoarding
import com.airbnb.lottie.LottieDrawable

class OnBoardingContent(
    override val inflateLayout: Int = R.layout.onboarding_content
) : AniTrendContent<OnboardingContentBinding>() {

    private val onBoarding by argument<OnBoarding>(PARAM_KEY)

    private fun requireArgument(): OnBoarding {
        return requireNotNull(onBoarding)
    }

    /**
     * Invoke view model observer to watch for changes
     */
    override fun setUpViewModelObserver() {

    }

    /**
     * Additional initialization to be done in this method, if the overriding class is type of
     * [androidx.fragment.app.Fragment] then this method will be called in
     * [androidx.fragment.app.FragmentActivity.onCreate]. Otherwise
     * [androidx.fragment.app.FragmentActivity.onPostCreate] invokes this function
     *
     * @param savedInstanceState
     */
    override fun initializeComponents(savedInstanceState: Bundle?) {
        lifecycleScope.launchWhenResumed {
            binding?.onBoardingLottieAnimationView?.playAnimation()
        }
    }

    /**
     * Called immediately after [onCreateView]
     * has returned, but before any saved state has been restored in to the view.
     * This gives subclasses a chance to initialize themselves once
     * they know their view hierarchy has been completely created. The fragment's
     * view hierarchy is not however attached to its parent at this point.
     *
     * @param view The View returned by [onCreateView].
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = OnboardingContentBinding.bind(view)

        view.background = context?.getCompatDrawable(requireArgument().background)

        requireBinding().onBoardingLottieAnimationView.repeatCount = LottieDrawable.INFINITE
        requireBinding().onBoardingLottieAnimationView.setAnimation(requireArgument().resource)

        requireBinding().onBoardingTitle.text = requireArgument().title
        requireBinding().onBoardingSubTitle.text = requireArgument().subTitle
        requireBinding().onBoardingDescription.text = requireArgument().description
    }

    companion object {
        private const val PARAM_KEY = "OnBoardingContent:OnBoarding"

        fun newInstance(model: OnBoarding): OnBoardingContent {
            val fragment = OnBoardingContent()
            fragment.arguments = bundleOf(PARAM_KEY to model)
            return fragment
        }
    }
}