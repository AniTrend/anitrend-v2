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

package co.anitrend.medialist.component.content

import android.os.Bundle
import android.view.View
import androidx.annotation.IntegerRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import co.anitrend.arch.recycler.adapter.SupportAdapter
import co.anitrend.arch.ui.view.widget.model.StateLayoutConfig
import co.anitrend.core.android.assureParamNotMissing
import co.anitrend.core.android.settings.extensions.flowUpdating
import co.anitrend.core.component.content.list.AniTrendListContent
import co.anitrend.core.extensions.orEmpty
import co.anitrend.data.settings.customize.ICustomizationSettings
import co.anitrend.data.settings.customize.common.PreferredViewMode
import co.anitrend.domain.media.entity.Media
import co.anitrend.medialist.R
import co.anitrend.medialist.component.content.viewmodel.MediaListViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class MediaListContent(
    private val settings: ICustomizationSettings,
    override val stateConfig: StateLayoutConfig,
    override val supportViewAdapter: SupportAdapter<Media>
) : AniTrendListContent<Media>() {

    private val viewModel by stateViewModel<MediaListViewModel>(
        state = { arguments.orEmpty() }
    )

    override val defaultSpanSize: Int
        get() = getSpanSizeByPreference(
            settings.preferredViewMode.value
        )

    @IntegerRes
    private fun getSpanSizeByPreference(
        viewMode: PreferredViewMode
    ) = when (viewMode) {
        PreferredViewMode.COMPACT -> co.anitrend.core.android.R.integer.column_x3
        PreferredViewMode.COMFORTABLE -> co.anitrend.core.android.R.integer.column_x2
        else -> co.anitrend.core.android.R.integer.column_x1
    }

    /**
     * Called immediately after [onCreateView] has returned, but before any saved state has been
     * restored in to the view. This gives subclasses a chance to initialize themselves once
     * they know their view hierarchy has been completely created. The fragment's view hierarchy
     * is not however attached to its parent at this point.
     *
     * @param view The View returned by [.onCreateView].
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                settings.preferredViewMode.flowUpdating(
                    listPresenter.recyclerView,
                    ::getSpanSizeByPreference,
                )
            }
        }
    }

    /**
     * Stub to trigger the loading of data, by default this is only called
     * when [supportViewAdapter] has no data in its underlying source.
     *
     * This is called when the fragment reaches it's [onStart] state
     *
     * @see initializeComponents
     */
    override fun onFetchDataInitialize() {
        listPresenter.stateLayout.assureParamNotMissing(viewModel.param) {
            viewModelState().invoke(requireNotNull(viewModel.param))
        }
    }

    /**
     * Invoke view model observer to watch for changes, this will be called
     * called in [onViewCreated]
     */
    override fun setUpViewModelObserver() {
        viewModelState().model.observe(viewLifecycleOwner) {
            onPostModelChange(it)
        }
        viewModel.filter.observe(viewLifecycleOwner) {
            if (it != null)
                viewModelState().invoke(it)
        }
    }

    /**
     * Proxy for a view model state if one exists
     */
    override fun viewModelState() = viewModel.state
}
