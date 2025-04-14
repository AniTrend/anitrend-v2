/*
 * Copyright (C) 2021 AniTrend
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
package co.anitrend.episode.component.sheet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import co.anitrend.core.component.viewmodel.state.AniTrendViewModelState
import co.anitrend.data.feed.episode.EpisodeDetailInteractor
import co.anitrend.domain.episode.entity.Episode
import co.anitrend.domain.episode.model.EpisodeParam
import kotlinx.coroutines.flow.map
import org.jsoup.Jsoup
import timber.log.Timber

class EpisodeSheetViewModel(
    private val interactor: EpisodeDetailInteractor,
) : AniTrendViewModelState<Episode>() {
    override val model: LiveData<Episode> =
        state.switchMap {
            Timber.i("Performing `model` switch map using ${viewModelScope.coroutineContext} on $this")
            it.model
                .map { model ->
                    val description = model.description.orEmpty()
                    val document = Jsoup.parse(description).html()
                    model.copy(description = document)
                }.asLiveData(viewModelScope.coroutineContext)
        }

    operator fun invoke(param: EpisodeParam.Detail) {
        val result = interactor(param)
        state.postValue(result)
    }
}
