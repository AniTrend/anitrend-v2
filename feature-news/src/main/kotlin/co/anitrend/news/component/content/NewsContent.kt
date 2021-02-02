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

import co.anitrend.arch.recycler.adapter.contract.ISupportAdapter
import co.anitrend.arch.ui.view.widget.model.StateLayoutConfig
import co.anitrend.core.component.content.list.AniTrendListContent
import co.anitrend.core.presenter.CorePresenter
import co.anitrend.core.util.locale.AniTrendLocale.Companion.asLocaleString
import co.anitrend.data.news.model.query.NewsQuery
import co.anitrend.domain.news.entity.News
import co.anitrend.news.R
import co.anitrend.news.component.content.viewmodel.NewsContentViewModel
import co.anitrend.news.presenter.NewsPresenter
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewsContent(
    private val presenter: NewsPresenter,
    override val stateConfig: StateLayoutConfig,
    override val supportViewAdapter: ISupportAdapter<News>,
    override val defaultSpanSize: Int = R.integer.single_list_size,
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
        viewModelState().invoke(
            NewsQuery(locale = presenter.getCurrentLocaleString())
        )
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