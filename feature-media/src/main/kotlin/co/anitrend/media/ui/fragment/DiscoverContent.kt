/*
 * Copyright (C) 2020  AniTrend
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

package co.anitrend.media.ui.fragment

import co.anitrend.arch.core.model.ISupportViewModelState
import co.anitrend.arch.recycler.adapter.contract.ISupportAdapter
import co.anitrend.arch.ui.view.widget.model.StateLayoutConfig
import co.anitrend.core.ui.fragment.list.AniTrendListFragment
import co.anitrend.media.R
import org.koin.android.ext.android.inject

class DiscoverContent(
    override val defaultSpanSize: Int = R.integer.single_list_size
) : AniTrendListFragment<Nothing>() {

    /**
     * State configuration for any underlying state representing widgets
     */
    override val stateConfig: StateLayoutConfig by inject()

    /**
     * Expects a module helper if one is available for the current scope, otherwise return null
     */
    override fun featureModuleHelper() = null

    /**
     * Stub to trigger the loading of data, by default this is only called
     * when [supportViewAdapter] has no data in its underlying source.
     *
     * This is called when the fragment reaches it's [onStart] state
     *
     * @see initializeComponents
     */
    override fun onFetchDataInitialize() {

    }

    /**
     * Invoke view model observer to watch for changes, this will be called
     * called in [onViewCreated]
     */
    override fun setUpViewModelObserver() {

    }

    /**
     * Proxy for a view model state if one exists
     */
    override fun viewModelState(): ISupportViewModelState<*>? = null

    /**
     * Adapter that should be used for the recycler view, by default [StateRestorationPolicy]
     * is set to [StateRestorationPolicy.PREVENT_WHEN_EMPTY]
     */
    override val supportViewAdapter: ISupportAdapter<Nothing>
        get() = TODO("Not yet implemented")
}