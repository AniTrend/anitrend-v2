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

package co.anitrend.media.discover.filter.component.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.arch.extension.ext.argument
import co.anitrend.arch.extension.ext.attachComponent
import co.anitrend.arch.recycler.SupportRecyclerView
import co.anitrend.arch.recycler.adapter.SupportAdapter
import co.anitrend.arch.recycler.adapter.SupportListAdapter
import co.anitrend.arch.recycler.adapter.contract.ISupportAdapter
import co.anitrend.arch.recycler.common.ClickableItem
import co.anitrend.arch.recycler.extensions.isEmpty
import co.anitrend.arch.recycler.shared.adapter.SupportLoadStateAdapter
import co.anitrend.arch.ui.fragment.list.contract.ISupportFragmentList
import co.anitrend.arch.ui.view.widget.model.StateLayoutConfig
import co.anitrend.core.android.recycler.decorator.DefaultSpacingDecorator
import co.anitrend.core.component.content.AniTrendContent
import co.anitrend.core.component.content.selection.AniTrendSelectionContent
import co.anitrend.core.extensions.combine
import co.anitrend.core.extensions.union
import co.anitrend.domain.common.sort.order.SortOrder
import co.anitrend.domain.genre.entity.Genre
import co.anitrend.domain.genre.model.GenreParam
import co.anitrend.media.discover.filter.R
import co.anitrend.media.discover.filter.component.viewmodel.genre.GenreViewModel
import co.anitrend.media.discover.filter.databinding.MediaDiscoverFilterGenreBinding
import co.anitrend.navigation.MediaDiscoverRouter
import com.google.android.flexbox.AlignContent
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

internal class GenreContent(
    override val stateConfig: StateLayoutConfig,
    override val supportViewAdapter: SupportAdapter<Genre>,
    override val inflateLayout: Int = R.layout.media_discover_filter_genre,
    override val bindingMapper: (View) -> MediaDiscoverFilterGenreBinding = {
        MediaDiscoverFilterGenreBinding.bind(it)
    }
) : AniTrendSelectionContent<MediaDiscoverFilterGenreBinding, Genre>() {

    private val param by argument(
        MediaDiscoverRouter.Param.KEY,
        MediaDiscoverRouter.Param()
    )

    private val viewModel by viewModel<GenreViewModel>()

    private fun applySelections(genres: List<Genre>) {
        // to avoid double triggers with the same data
        if (supportViewAdapter.isEmpty()) {
            val items = param.genre_in.combine(param.genre)

            val selectedIds = genres.union(items) { p, s ->
                s == p.name
            }.map(Genre::id)

            supportViewAdapter.supportAction?.selectAllItems(selectedIds)
        }
    }

    /**
     * Stub to trigger the loading of data, by default this is only called
     * when [supportViewAdapter] has no data in its underlying source.
     *
     * This is called when the fragment reaches it's [onResume] state
     *
     * @see initializeComponents
     */
    override fun onFetchDataInitialize() {
        val param = GenreParam(SortOrder.ASC)
        viewModelState().invoke(param)
    }

    /**
     * Invoke view model observer to watch for changes, this will be called
     * called in [onViewCreated]
     */
    override fun setUpViewModelObserver() {
        viewModelState().model.observe(viewLifecycleOwner) {
            applySelections(it)
            onPostModelChange(it)
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
        requireBinding().genresRecyclerSelection.configure()
    }

    /**
     * Proxy for a view model state if one exists
     */
    override fun viewModelState() = viewModel.state
}
