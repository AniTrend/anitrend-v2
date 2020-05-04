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
import co.anitrend.arch.extension.*
import co.anitrend.core.extensions.hideStatusBarAndNavigationBar
import co.anitrend.core.extensions.injectScoped
import co.anitrend.core.ui.activity.AnitrendActivity
import co.anitrend.onboarding.databinding.OnboardingScreenBinding
import co.anitrend.onboarding.koin.moduleHelper
import co.anitrend.onboarding.presenter.OnBoardingPresenter
import co.anitrend.onboarding.ui.pager.OnBoardingPageAdapter

class OnBoardingScreen : AnitrendActivity() {

    private lateinit var binding: OnboardingScreenBinding

    private val onBoardingPageAdapter by lazy(LAZY_MODE_UNSAFE) {
        OnBoardingPageAdapter(
            presenter.onBoardingItems,
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

    private val presenter by injectScoped<OnBoardingPresenter>()

    override fun configureActivity() {
        super.configureActivity()
        hideStatusBarAndNavigationBar()
        attachComponent(moduleHelper)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = OnboardingScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initializeComponents(savedInstanceState: Bundle?) {
        onUpdateUserInterface()
        binding.finishOnBoarding.setOnClickListener {
            presenter.onBoardingExperienceCompleted()
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

    override fun onResume() {
        super.onResume()
        binding.liquidSwipeViewPager.addOnPageChangeListener(pageChangeListener)
    }

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

    override fun onDestroy() {
        super.onDestroy()
        detachComponent(moduleHelper)
    }

    companion object {
        private const val PAGER_STATE_KEY = "OnBoardingScreen_PAGER_STATE_KEY"
    }
}