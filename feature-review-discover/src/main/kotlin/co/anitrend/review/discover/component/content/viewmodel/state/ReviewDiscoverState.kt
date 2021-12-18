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

package co.anitrend.review.discover.component.content.viewmodel.state

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.asLiveData
import androidx.paging.PagedList
import kotlin.properties.Delegates
import kotlin.coroutines.CoroutineContext
import co.anitrend.arch.core.model.ISupportViewModelState
import co.anitrend.arch.data.state.DataState
import co.anitrend.data.review.GetReviewPagedInteractor
import co.anitrend.domain.review.entity.Review
import co.anitrend.domain.review.model.ReviewParam
import co.anitrend.navigation.ReviewDiscoverRouter

class ReviewDiscoverState(
    private val interactor: GetReviewPagedInteractor
): ISupportViewModelState<PagedList<Review>> {

    var context by Delegates.notNull<CoroutineContext>()
    private val useCaseResult = MutableLiveData<DataState<PagedList<Review>>>()

    override val model = Transformations.switchMap(useCaseResult) {
        it.model.asLiveData(context)
    }

    override val loadState = Transformations.switchMap(useCaseResult) {
        it.loadState.asLiveData(context)
    }

    override val refreshState = Transformations.switchMap(useCaseResult) {
        it.refreshState.asLiveData(context)
    }
    
    operator fun invoke(param: ReviewDiscoverRouter.Param) {
        val query = ReviewParam.Paged(
            mediaId = param.mediaId,
            userId = param.userId,
            mediaType = param.mediaType,
            sort = param.sort,
            scoreFormat = param.scoreFormat
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