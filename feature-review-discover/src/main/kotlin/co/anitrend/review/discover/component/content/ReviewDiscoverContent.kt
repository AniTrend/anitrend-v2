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

package co.anitrend.review.discover.component.content

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import co.anitrend.arch.recycler.adapter.SupportAdapter
import co.anitrend.arch.ui.view.widget.model.StateLayoutConfig
import co.anitrend.core.android.assureParamNotMissing
import co.anitrend.core.component.content.list.AniTrendListContent
import co.anitrend.core.extensions.orEmpty
import co.anitrend.domain.review.entity.Review
import co.anitrend.review.discover.R
import co.anitrend.review.discover.component.content.viewmodel.ReviewDiscoverViewModel
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class ReviewDiscoverContent(
    override val stateConfig: StateLayoutConfig,
    override val inflateMenu: Int = co.anitrend.core.android.R.menu.discover_menu,
    override val supportViewAdapter: SupportAdapter<Review>,
    override val defaultSpanSize: Int = co.anitrend.core.android.R.integer.column_x1
) : AniTrendListContent<Review>() {

    private val viewModel by stateViewModel<ReviewDiscoverViewModel>(
        state = { arguments.orEmpty() }
    )

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.findItem(co.anitrend.core.android.R.id.action_list_style).isVisible = false
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     *
     * Derived classes should call through to the base class for it to
     * perform the default menu handling.
     *
     * @param item The menu item that was selected.
     *
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     *
     * @see .onCreateOptionsMenu
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            co.anitrend.core.android.R.id.action_filter -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
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
        listPresenter.stateLayout.assureParamNotMissing(viewModelState().default) {
            viewModelState().invoke(viewModelState().default)
        }
    }

    /**
     * Proxy for a view model state if one exists
     */
    override fun viewModelState() = viewModel.state
}
