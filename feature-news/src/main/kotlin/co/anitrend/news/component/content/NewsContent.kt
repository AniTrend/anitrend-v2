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

package co.anitrend.news.component.content

import androidx.recyclerview.widget.RecyclerView
import co.anitrend.arch.recycler.SupportRecyclerView
import co.anitrend.arch.recycler.adapter.SupportAdapter
import co.anitrend.arch.recycler.shared.adapter.SupportLoadStateAdapter
import co.anitrend.arch.ui.view.widget.model.StateLayoutConfig
import co.anitrend.core.android.settings.helper.locale.AniTrendLocale.Companion.asLocaleString
import co.anitrend.core.component.content.list.AniTrendListContent
import co.anitrend.domain.news.entity.News
import co.anitrend.domain.news.model.NewsParam
import co.anitrend.news.R
import co.anitrend.news.component.content.viewmodel.NewsContentViewModel
import co.anitrend.news.presenter.NewsPresenter
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewsContent(
    private val presenter: NewsPresenter,
    override val stateConfig: StateLayoutConfig,
    override val supportViewAdapter: SupportAdapter<News>,
    override val defaultSpanSize: Int = R.integer.column_x1,
) : AniTrendListContent<News>() {

    private val viewModel by viewModel<NewsContentViewModel>()

    /**
     * Stub to trigger the loading of data, by default this is only called
     * when [supportViewAdapter] has no data in its underlying source.
     *
     * This is called when the fragment reaches it's [onResume] state
     *
     * @see initializeComponents
     */
    override fun onFetchDataInitialize() {
        val locale = presenter.settings.locale.value.asLocaleString()
        viewModelState().invoke(
            NewsParam(locale)
        )
    }

    /**
     * Sets the adapter for the recycler view
     */
    override fun setRecyclerAdapter(recyclerView: SupportRecyclerView) {
        if (recyclerView.adapter == null) {
            val header = SupportLoadStateAdapter(resources, stateConfig).apply {
                registerFlowListener()
            }

            if (supportViewAdapter is RecyclerView.Adapter<*>) {
                (supportViewAdapter as RecyclerView.Adapter<*>)
                    .stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }

            recyclerView.adapter = supportViewAdapter.withLoadStateHeader(header = header)
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