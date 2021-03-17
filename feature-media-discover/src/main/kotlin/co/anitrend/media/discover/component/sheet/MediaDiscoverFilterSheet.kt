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
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.setFragmentResultListener
import co.anitrend.arch.extension.ext.UNSAFE
import co.anitrend.core.component.sheet.AniTrendBottomSheet
import co.anitrend.media.discover.R
import co.anitrend.media.discover.component.content.viewmodel.MediaDiscoverViewModel
import co.anitrend.media.discover.component.sheet.action.MediaFilterAction
import co.anitrend.media.discover.component.sheet.controller.MediaFilterController
import co.anitrend.media.discover.databinding.MediaDiscoverFilterSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ViewModelOwner.Companion.from
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MediaDiscoverFilterSheet(
    override val inflateLayout: Int = R.layout.media_discover_filter_sheet
) : AniTrendBottomSheet<MediaDiscoverFilterSheetBinding>() {

    private val viewModel by sharedViewModel<MediaDiscoverViewModel>(
        owner = { from(requireParentFragment(), requireParentFragment()) }
    )

    private val closeSheetOnBackPressed =
        object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {
                dismiss()
            }
        }

    private val controller by lazy(UNSAFE) { MediaFilterController() }

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
        controller.registerResultCallbacks(this, viewModel.filter)
    }

    /**
     * Invoke view model observer to watch for changes, this will be called
     * called in [onViewCreated]
     */
    override fun setUpViewModelObserver() {

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
    }
}