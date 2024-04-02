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
package co.anitrend.onboarding.component.screen

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import co.anitrend.arch.extension.ext.hideStatusBarAndNavigationBar
import co.anitrend.core.component.screen.AniTrendBoundScreen
import co.anitrend.core.ui.inject
import co.anitrend.onboarding.component.pager.OnBoardingPageAdapter
import co.anitrend.onboarding.component.presenter.OnBoardingPresenter
import co.anitrend.onboarding.databinding.OnboardingScreenBinding
import kotlinx.coroutines.launch

class OnBoardingScreen : AniTrendBoundScreen<OnboardingScreenBinding>() {
    private val presenter by inject<OnBoardingPresenter>()

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
                positionOffsetPixels: Int,
            ) {
                // TODO: Updating motion layout progress seems to mess up the view pager
                lifecycleScope.launch {
                    val newProgress = (position + positionOffset) / presenter.pages
                    binding?.motionLayout?.progress = newProgress
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

    override fun configureActivity() {
        super.configureActivity()
        hideStatusBarAndNavigationBar()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = OnboardingScreenBinding.inflate(layoutInflater)
        setContentView(requireBinding().root)
    }

    override fun initializeComponents(savedInstanceState: Bundle?) {
        onUpdateUserInterface()
        binding?.onbaordingButtonNext?.setOnClickListener {
            val nextSlidePos: Int = requireBinding().liquidSwipeViewPager.currentItem.plus(1)
            requireBinding().liquidSwipeViewPager.setCurrentItem(nextSlidePos, true)
        }
        binding?.onbaordingButtonSkip?.setOnClickListener {
            requireBinding().liquidSwipeViewPager.setCurrentItem(presenter.pages, true)
        }
        binding?.onbaordingButtonGetStarted?.setOnClickListener {
            presenter.onBoardingExperienceCompleted()
            finishAfterTransition()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val currentItem = binding?.liquidSwipeViewPager?.currentItem
        if (currentItem != null) {
            outState.putInt(PAGER_STATE_KEY, currentItem)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val lastPosition = savedInstanceState.getInt(PAGER_STATE_KEY, 0)
        if (binding?.liquidSwipeViewPager?.currentItem != lastPosition) {
            binding?.liquidSwipeViewPager?.setCurrentItem(
                lastPosition,
                false,
            )
        }
    }

    override fun onResume() {
        super.onResume()
        binding?.liquidSwipeViewPager?.addOnPageChangeListener(pageChangeListener)
    }

    override fun onPause() {
        binding?.liquidSwipeViewPager?.removeOnPageChangeListener(pageChangeListener)
        super.onPause()
    }

    private fun onUpdateUserInterface() {
        // workaround, for some reason updating motion progress messes up the view pager
        binding?.liquidSwipeViewPager?.offscreenPageLimit = presenter.pages - 1
        binding?.liquidSwipeViewPager?.adapter =
            OnBoardingPageAdapter(
                presenter.onBoardingItems,
                this,
                supportFragmentManager,
            )
        /*binding.liquidSwipeViewPager.setPageTransformer(
            false
        ) { page, position ->
            when {
                position < -1 ->age.alpha = 1f
                position <= 1 ->
                    page.onBoardingLottieAnimationView.translationX = -position * (page.width / 2)
                else -> page.alpha = 1f
            }
        }*/
        binding?.inkPageIndicator?.setViewPager(binding?.liquidSwipeViewPager)
    }

    override fun onDestroy() {
        binding?.onbaordingButtonNext?.setOnClickListener(null)
        binding?.onbaordingButtonSkip?.setOnClickListener(null)
        binding?.onbaordingButtonGetStarted?.setOnClickListener(null)
        super.onDestroy()
    }

    companion object {
        private const val PAGER_STATE_KEY = "OnBoardingScreen_PAGER_STATE_KEY"
    }
}
