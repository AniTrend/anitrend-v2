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

package co.anitrend.medialist.component.content.viewmodel.state

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.asLiveData
import androidx.paging.PagedList
import co.anitrend.arch.core.model.ISupportViewModelState
import co.anitrend.arch.data.state.DataState
import co.anitrend.data.medialist.GetPagedMediaListInteractor
import co.anitrend.data.user.settings.IUserSettings
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.medialist.model.MediaListParam
import co.anitrend.navigation.MediaListRouter
import kotlin.coroutines.CoroutineContext
import kotlin.properties.Delegates

class MediaListState(
    private val interactor: GetPagedMediaListInteractor,
    private val settings: IUserSettings
) : ISupportViewModelState<PagedList<Media>> {

    var context by Delegates.notNull<CoroutineContext>()

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

    operator fun invoke(param: MediaListRouter.Param) {
        val query = MediaListParam.Paged(
            customListName = param.customListName,
            mediaId_in = param.mediaId_in,
            mediaId_not_in = param.mediaId_not_in,
            isFollowing = param.isFollowing,
            userId_in = param.userId_in,
            compareWithAuthList = param.compareWithAuthList,
            scoreFormat = settings.scoreFormat.value,
            type = param.type,
            userId = param.userId,
            userName = param.userName,
            completedAt = param.completedAt,
            completedAt_greater = param.completedAt_greater,
            completedAt_lesser = param.completedAt_lesser,
            completedAt_like = param.completedAt_like,
            notes = param.notes,
            notes_like = param.notes_like,
            sort = param.sort,
            startedAt = param.startedAt,
            startedAt_greater = param.startedAt_greater,
            startedAt_lesser = param.startedAt_lesser,
            startedAt_like = param.startedAt_like,
            status = param.status,
            status_in = param.status_in,
            status_not = param.status_not,
            status_not_in = param.status_not_in,
        )

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