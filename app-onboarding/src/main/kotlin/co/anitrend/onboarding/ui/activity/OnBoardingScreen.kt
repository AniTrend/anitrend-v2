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

package co.anitrend.onboarding.ui.activity

import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import co.anitrend.arch.extension.LAZY_MODE_UNSAFE
import co.anitrend.arch.extension.gone
import co.anitrend.arch.extension.invisible
import co.anitrend.arch.extension.visible
import co.anitrend.core.extensions.hideStatusBarAndNavigationBar
import co.anitrend.core.ui.activity.AnitrendActivity
import co.anitrend.onboarding.databinding.OnboardingScreenBinding
import co.anitrend.onboarding.koin.injectFeatureModules
import co.anitrend.onboarding.presenter.OnBoardingPresenter
import co.anitrend.onboarding.ui.pager.OnBoardingPageAdapter
import org.koin.android.ext.android.inject

class OnBoardingScreen : AnitrendActivity<Nothing, OnBoardingPresenter>() {

    private lateinit var binding: OnboardingScreenBinding

    private val onBoardingPageAdapter by lazy(LAZY_MODE_UNSAFE) {
        OnBoardingPageAdapter(
            supportPresenter.onBoardingItems,
            this
        )
    }

    private val pageChangeListener =
        object : ViewPager.OnPageChangeListener {
            /**
             * Called when the scroll state changes. Useful for discovering when the user
             * begins dragging, when the pager is automatically settling to the current page,
             * or when it is fully stopped/idle.
             *
             * @param state The new scroll state.
             * @see ViewPager.SCROLL_STATE_IDLE
             *
             * @see ViewPager.SCROLL_STATE_DRAGGING
             *
             * @see ViewPager.SCROLL_STATE_SETTLING
             */
            override fun onPageScrollStateChanged(state: Int) {

            }

            /**
             * This method will be invoked when the current page is scrolled, either as part
             * of a programmatically initiated smooth scroll or a user initiated touch scroll.
             *
             * @param position Position index of the first page currently being displayed.
             * Page position+1 will be visible if positionOffset is nonzero.
             * @param positionOffset Value from [0, 1) indicating the offset from the page at position.
             * @param positionOffsetPixels Value in pixels indicating the offset from position.
             */
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                if (position == 5) {
                    binding.finishOnBoarding.visible()
                    binding.inkPageIndicator.invisible()
                } else {
                    binding.finishOnBoarding.gone()
                    binding.inkPageIndicator.visible()
                }
            }

            /**
             * This method will be invoked when a new page becomes selected. Animation is not
             * necessarily complete.
             *
             * @param position Position index of the new selected page.
             */
            override fun onPageSelected(position: Int) {

            }
        }

    /**
     * Should be created lazily through injection or lazy delegate
     *
     * @return supportPresenter of the generic type specified
     */
    override val supportPresenter by inject<OnBoardingPresenter>()

    /**
     * Can be used to configure custom theme styling as desired
     */
    override fun configureActivity() {
        super.configureActivity()
        hideStatusBarAndNavigationBar()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = OnboardingScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
        injectFeatureModules()
        onUpdateUserInterface()
        binding.finishOnBoarding.setOnClickListener {
            supportPresenter.onBoardingExperienceCompleted()
            finishAfterTransition()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(
            PAGER_STATE_KEY,
            binding.liquidSwipeViewPager.currentItem
        )
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val lastPosition = savedInstanceState.getInt(
            PAGER_STATE_KEY,
            0
        )
        if (binding.liquidSwipeViewPager.currentItem != lastPosition)
            binding.liquidSwipeViewPager.setCurrentItem(
                lastPosition,
                false
            )
    }

    /**
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are *not* resumed.
     */
    override fun onResume() {
        super.onResume()
        binding.liquidSwipeViewPager.addOnPageChangeListener(pageChangeListener)
    }

    /**
     * Dispatch onPause() to fragments.
     */
    override fun onPause() {
        binding.liquidSwipeViewPager.removeOnPageChangeListener(pageChangeListener)
        super.onPause()
    }

    /**
     * Handles the updating of views, binding, creation or state change, depending on the context
     * [androidx.lifecycle.LiveData] for a given [ISupportFragmentActivity] will be available by this point.
     *
     * Check implementation for more details
     */
    override fun onUpdateUserInterface() {
        binding.liquidSwipeViewPager.offscreenPageLimit = 1
        binding.liquidSwipeViewPager.adapter = onBoardingPageAdapter
        binding.inkPageIndicator.setViewPager(binding.liquidSwipeViewPager)
    }

    companion object {
        private const val PAGER_STATE_KEY = "OnBoardingScreen_PAGER_STATE_KEY"
    }
}