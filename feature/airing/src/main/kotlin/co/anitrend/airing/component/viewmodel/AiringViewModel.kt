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
package co.anitrend.airing.component.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import co.anitrend.data.airing.GetPagingAiringScheduleInteractor
import co.anitrend.domain.airing.model.AiringParam
import co.anitrend.domain.media.entity.Media
import co.anitrend.navigation.AiringRouter
import co.anitrend.navigation.extensions.nameOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest

class AiringViewModel(
    private val stateHandle: SavedStateHandle,
    private val interactor: GetPagingAiringScheduleInteractor,
) : ViewModel() {
    val initialParam =
        stateHandle.get<AiringRouter.AiringParam>(
            nameOf<AiringRouter.AiringParam>(),
        ) ?: AiringRouter.AiringParam()

    val filter: StateFlow<AiringRouter.AiringParam> =
        stateHandle.getStateFlow(
            key = FILTER_STATE_KEY,
            initialValue = initialParam,
        )

    val schedule: Flow<PagingData<Media>> =
        filter
            .flatMapLatest(::query)
            .cachedIn(viewModelScope)

    fun resetFilter() {
        setFilter(initialParam)
    }

    fun setFilter(param: AiringRouter.AiringParam) {
        stateHandle[FILTER_STATE_KEY] = param
    }

    private fun query(param: AiringRouter.AiringParam): Flow<PagingData<Media>> =
        interactor(
            AiringParam.Find(
                id = param.id,
                mediaId = param.mediaId,
                episode = param.episode,
                airingAt = param.airingAt,
                notYetAired = param.notYetAired,
                id_not = param.id_not,
                id_in = param.id_in,
                id_not_in = param.id_not_in,
                mediaId_not = param.mediaId_not,
                mediaId_in = param.mediaId_in,
                mediaId_not_in = param.mediaId_not_in,
                episode_not = param.episode_not,
                episode_in = param.episode_in,
                episode_not_in = param.episode_not_in,
                episode_greater = param.episode_greater,
                episode_lesser = param.episode_lesser,
                airingAt_greater = param.airingAt_greater,
                airingAt_lesser = param.airingAt_lesser,
                sort = param.sort,
            ),
        )

    companion object {
        private const val FILTER_STATE_KEY = "airing_filter_state"
    }
}
