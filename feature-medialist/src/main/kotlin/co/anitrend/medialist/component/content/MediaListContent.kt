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

import androidx.annotation.IntegerRes
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import co.anitrend.arch.recycler.adapter.SupportAdapter
import co.anitrend.arch.ui.view.widget.model.StateLayoutConfig
import co.anitrend.core.android.assureParamNotMissing
import co.anitrend.core.component.content.list.AniTrendListContent
import co.anitrend.core.extensions.orEmpty
import co.anitrend.data.settings.customize.ICustomizationSettings
import co.anitrend.data.settings.customize.common.PreferredViewMode
import co.anitrend.domain.media.entity.Media
import co.anitrend.medialist.R
import co.anitrend.medialist.component.content.viewmodel.MediaListViewModel
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaListContent(
    private val settings: ICustomizationSettings,
    override val stateConfig: StateLayoutConfig,
    override val supportViewAdapter: SupportAdapter<Media>
) : AniTrendListContent<Media>() {

    private val viewModel by viewModel<MediaListViewModel>(
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
        PreferredViewMode.COMPACT -> R.integer.column_x3
        PreferredViewMode.COMFORTABLE -> R.integer.column_x2
        else -> R.integer.column_x1
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
        lifecycleScope.launchWhenResumed {
            settings.preferredViewMode.flow.collect {
                val layoutManger = listPresenter.recyclerView.layoutManager
                if (layoutManger is StaggeredGridLayoutManager) {
                    val currentSpanCount = layoutManger.spanCount
                    val newSpanCount = resources.getInteger(
                        getSpanSizeByPreference(it)
                    )
                    if (currentSpanCount != newSpanCount)
                        layoutManger.spanCount = newSpanCount
                    else supportViewAdapter.notifyDataSetNeedsRefreshing()
                }
            }
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