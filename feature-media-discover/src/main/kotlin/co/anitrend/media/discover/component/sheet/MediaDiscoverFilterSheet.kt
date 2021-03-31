/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.media.discover.component.sheet

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.children
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import co.anitrend.arch.extension.ext.UNSAFE
import co.anitrend.arch.extension.ext.getStringList
import co.anitrend.core.android.animations.normalize
import co.anitrend.core.android.components.sheet.action.OnSlideAction
import co.anitrend.core.component.sheet.AniTrendBottomSheet
import co.anitrend.media.discover.R
import co.anitrend.media.discover.component.content.viewmodel.MediaDiscoverViewModel
import co.anitrend.media.discover.component.sheet.adapter.FilterPageAdapter
import co.anitrend.media.discover.component.sheet.controller.MediaFilterController
import co.anitrend.media.discover.databinding.MediaDiscoverFilterSheetBinding
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ViewModelOwner.Companion.from
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MediaDiscoverFilterSheet(
    override val inflateLayout: Int = R.layout.media_discover_filter_sheet
) : AniTrendBottomSheet<MediaDiscoverFilterSheetBinding>() {

    private val viewModel by sharedViewModel<MediaDiscoverViewModel>(
        owner = { from(requireParentFragment(), requireParentFragment()) }
    )

    private val titles by lazy(UNSAFE) {
        requireContext().getStringList(R.array.titles_filter_pages)
    }

    private val closeSheetOnBackPressed =
        object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {
                dismiss()
            }
        }

    private val controller by lazy(UNSAFE) {
        MediaFilterController(requireBinding().materialTabsLayout)
    }

    private fun setUpViewPager() {
        requireBinding().viewPager.adapter = FilterPageAdapter(
            viewModel.filter.value,
            titles,
            requireActivity(),
            childFragmentManager,
            lifecycle
        )
        // temporary work around for nested scrolling not working as expected
        requireBinding().viewPager.children.find {
            it is RecyclerView
        }?.let { it.isNestedScrollingEnabled = false }
    }

    /**
     * Additional initialization to be done in this method, this method will be called in
     * [androidx.fragment.app.FragmentActivity.onCreate].
     *
     * @param savedInstanceState
     */
    override fun initializeComponents(savedInstanceState: Bundle?) {
        requireActivity().onBackPressedDispatcher.addCallback(
            this, closeSheetOnBackPressed
        )
    }

    /**
     * Invoke view model observer to watch for changes, this will be called
     * called in [onViewCreated]
     */
    override fun setUpViewModelObserver() {
        viewModel.filter.observe(viewLifecycleOwner) { param ->
            viewModel.setParam(param)
        }
    }

    /**
     * Called immediately after [onCreateView] has returned, but before any saved state has been
     * restored in to the view. This gives subclasses a chance to initialize themselves once
     * they know their view hierarchy has been completely created.
     *
     * The fragment's view hierarchy is not however attached to its parent at this point.
     *
     * @param view The View returned by [onCreateView].
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
     * saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MediaDiscoverFilterSheetBinding.bind(view)
        setUpViewPager()
        TabLayoutMediator(
            requireBinding().materialTabsLayout,
            requireBinding().viewPager
        ) { tab, index -> tab.text = titles[index] }.attach()
        controller.registerResultCallbacks(this, viewModel.filter)
        bottomSheetCallback.addOnSlideAction(
            object: OnSlideAction {
                override fun onSlide(sheet: View, slideOffset: Float) {
                    val alpha = slideOffset.normalize(-1F, 0F, 0F, 1F)
                    requireBinding().sheetHandle.alpha = 1F - alpha
                }
            }
        )
    }
}