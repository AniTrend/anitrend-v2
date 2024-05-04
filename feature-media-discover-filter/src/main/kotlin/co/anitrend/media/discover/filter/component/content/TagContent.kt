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
import android.view.View
import co.anitrend.arch.extension.ext.argument
import co.anitrend.arch.recycler.adapter.SupportAdapter
import co.anitrend.arch.recycler.extensions.isEmpty
import co.anitrend.arch.ui.view.widget.model.StateLayoutConfig
import co.anitrend.core.component.content.selection.AniTrendSelectionContent
import co.anitrend.core.extensions.combine
import co.anitrend.core.extensions.union
import co.anitrend.domain.common.sort.order.SortOrder
import co.anitrend.domain.tag.entity.Tag
import co.anitrend.domain.tag.model.TagParam
import co.anitrend.media.discover.filter.R
import co.anitrend.media.discover.filter.component.viewmodel.tag.TagViewModel
import co.anitrend.media.discover.filter.databinding.MediaDiscoverFilterTagBinding
import co.anitrend.navigation.MediaDiscoverRouter
import org.koin.androidx.viewmodel.ext.android.viewModel

internal class TagContent(
    override val stateConfig: StateLayoutConfig,
    override val supportViewAdapter: SupportAdapter<Tag>,
    override val inflateLayout: Int = R.layout.media_discover_filter_tag,
    override val bindingMapper: (View) -> MediaDiscoverFilterTagBinding = {
        MediaDiscoverFilterTagBinding.bind(it)
    }
) : AniTrendSelectionContent<MediaDiscoverFilterTagBinding, Tag>() {

    private val param by argument(MediaDiscoverRouter::MediaDiscoverParam)

    private val viewModel by viewModel<TagViewModel>()

    private fun applySelections(tags: List<Tag>) {
        // to avoid double triggers with the same data
        if (supportViewAdapter.isEmpty()) {
            val tagParams = param.tag_in.combine(param.tag)

            val tagSelectedIds = tags.union(tagParams) { p, s ->
                p.name == s
            }.map(Tag::id)

            val categories = param.tagCategory_in.combine(param.tagCategory)

            val categorySelectedIds = tags.union(categories) { p, s ->
                p.category == s
            }.map(Tag::id)

            val selectedIds = (tagSelectedIds + categorySelectedIds).distinct()
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
        val param = TagParam(SortOrder.ASC)
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
        requireBinding().tagsRecyclerSelection.configure()
    }

    /**
     * Proxy for a view model state if one exists
     */
    override fun viewModelState() = viewModel.state
}
