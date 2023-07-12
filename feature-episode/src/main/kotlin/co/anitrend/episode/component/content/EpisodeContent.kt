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

package co.anitrend.episode.component.content

import co.anitrend.arch.recycler.adapter.SupportAdapter
import co.anitrend.arch.ui.view.widget.model.StateLayoutConfig
import co.anitrend.core.android.settings.common.locale.ILocaleSettings
import co.anitrend.core.android.settings.helper.locale.model.AniTrendLocale.Companion.asLocaleString
import co.anitrend.core.component.content.list.AniTrendListContent
import co.anitrend.domain.episode.entity.Episode
import co.anitrend.domain.episode.model.EpisodeParam
import co.anitrend.episode.component.content.viewmodel.EpisodeContentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class EpisodeContent(
    private val settings: ILocaleSettings,
    override val stateConfig: StateLayoutConfig,
    override val supportViewAdapter: SupportAdapter<Episode>,
    override val defaultSpanSize: Int = co.anitrend.core.android.R.integer.column_x1,
) : AniTrendListContent<Episode>() {

    private val viewModel by viewModel<EpisodeContentViewModel>()

    /**
     * Stub to trigger the loading of data, by default this is only called
     * when [supportViewAdapter] has no data in its underlying source.
     *
     * This is called when the fragment reaches it's [onResume] state
     *
     * @see initializeComponents
     */
    override fun onFetchDataInitialize() {
        val locale = settings.locale.value.asLocaleString()
        viewModelState().invoke(
            EpisodeParam.Paged(locale)
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
