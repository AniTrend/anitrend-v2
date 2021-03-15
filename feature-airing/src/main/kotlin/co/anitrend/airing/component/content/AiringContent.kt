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

package co.anitrend.airing.component.content

import co.anitrend.airing.R
import co.anitrend.airing.component.viewmodel.AiringViewModel
import co.anitrend.arch.extension.ext.argument
import co.anitrend.arch.recycler.adapter.contract.ISupportAdapter
import co.anitrend.arch.ui.view.widget.model.StateLayoutConfig
import co.anitrend.core.android.assureParamNotMissing
import co.anitrend.core.component.content.list.AniTrendListContent
import co.anitrend.domain.media.entity.Media
import co.anitrend.navigation.AiringRouter
import org.koin.androidx.viewmodel.ext.android.viewModel

class AiringContent(
    override val stateConfig: StateLayoutConfig,
    override val supportViewAdapter: ISupportAdapter<Media>,
    override val defaultSpanSize: Int = R.integer.grid_list_x2
) : AniTrendListContent<Media>() {

    private val viewModel by viewModel<AiringViewModel>()

    private val param: AiringRouter.Param? by argument(AiringRouter.Param.KEY)

    /**
     * Stub to trigger the loading of data, by default this is only called
     * when [supportViewAdapter] has no data in its underlying source.
     *
     * This is called when the fragment reaches it's [onStart] state
     *
     * @see initializeComponents
     */
    override fun onFetchDataInitialize() {
        supportStateLayout?.assureParamNotMissing(param) {
            viewModelState().invoke(requireNotNull(param))
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
     * Proxy for a view model state if one exists
     */
    override fun viewModelState() = viewModel.state
}