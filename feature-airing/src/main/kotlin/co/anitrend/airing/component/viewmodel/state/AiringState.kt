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

package co.anitrend.airing.component.viewmodel.state

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.asLiveData
import androidx.paging.PagedList
import co.anitrend.arch.data.state.DataState
import co.anitrend.core.component.viewmodel.AniTrendViewModelState
import co.anitrend.data.airing.GetPagedAiringScheduleInteractor
import co.anitrend.domain.airing.model.AiringParam
import co.anitrend.domain.media.entity.Media
import co.anitrend.navigation.AiringRouter

class AiringState(
    private val interactor: GetPagedAiringScheduleInteractor
) : AniTrendViewModelState<PagedList<Media>>() {

    private val useCaseResult = MutableLiveData<DataState<PagedList<Media>>>()

    override val model = Transformations.switchMap(useCaseResult) {
        it.model.asLiveData(context)
    }

    override val loadState = Transformations.switchMap(useCaseResult) {
        it.loadState.asLiveData(context)
    }

    override val refreshState = Transformations.switchMap(useCaseResult) {
        it.refreshState.asLiveData(context)
    }

    operator fun invoke(param: AiringRouter.Param) {
        val query = AiringParam.Find() builder {
            id = param.id
            mediaId = param.mediaId
            episode = param.episode
            airingAt = param.airingAt
            notYetAired = param.notYetAired
            id_not = param.id_not
            id_in = param.id_in
            id_not_in = param.id_not_in
            mediaId_not = param.mediaId_not
            mediaId_in = param.mediaId_in
            mediaId_not_in = param.mediaId_not_in
            episode_not = param.episode_not
            episode_in = param.episode_in
            episode_not_in = param.episode_not_in
            episode_greater = param.episode_greater
            episode_lesser = param.episode_lesser
            airingAt_greater = param.airingAt_greater
            airingAt_lesser = param.airingAt_lesser
            sort = param.sort
        }
        val result = interactor(query)
        useCaseResult.postValue(result)
    }

    /**
     * Called upon [androidx.lifecycle.ViewModel.onCleared] and should optionally
     * call cancellation of any ongoing jobs.
     *
     * If your use case source is of type [co.anitrend.arch.domain.common.IUseCase]
     * then you could optionally call [co.anitrend.arch.domain.common.IUseCase.onCleared] here
     */
    override fun onCleared() {
        interactor.onCleared()
    }

    /**
     * Triggers use case to perform refresh operation
     */
    override suspend fun refresh() {
        val uiModel = useCaseResult.value
        uiModel?.refresh?.invoke()
    }

    /**
     * Triggers use case to perform a retry operation
     */
    override suspend fun retry() {
        val uiModel = useCaseResult.value
        uiModel?.retry?.invoke()
    }
}