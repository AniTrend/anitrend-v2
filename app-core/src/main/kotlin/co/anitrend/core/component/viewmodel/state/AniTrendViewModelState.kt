/*
 * Copyright (C) 2022  AniTrend
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

package co.anitrend.core.component.viewmodel.state

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import co.anitrend.arch.core.model.ISupportViewModelState
import co.anitrend.arch.data.state.DataState
import co.anitrend.arch.domain.common.IUseCase
import co.anitrend.arch.extension.coroutine.ISupportCoroutine
import co.anitrend.arch.extension.coroutine.extension.Default
import kotlinx.coroutines.flow.merge
import timber.log.Timber

abstract class AniTrendViewModelState<T> : ISupportViewModelState<T>,
    ISupportCoroutine by Default() {

    protected val state = MutableLiveData<DataState<T>>()

    protected abstract val interactor: IUseCase

    override val model = state.switchMap {
        Timber.i("Performing `model` switch map using $coroutineContext on $this")
        it.model.asLiveData(coroutineContext)
    }

    override val loadState = state.switchMap {
        Timber.i("Performing `loadState` switch map using $coroutineContext on $this")
        it.loadState.asLiveData(coroutineContext)
    }

    override val refreshState = state.switchMap {
        Timber.i("Performing `refreshState` switch map using $coroutineContext on $this")
        it.refreshState.asLiveData(coroutineContext)
    }

    val combinedLoadState = state.switchMap {
        Timber.i("Performing `combinedLoadState` switch map using $coroutineContext on $this")
        val result = merge(it.loadState, it.refreshState)
        result.asLiveData(coroutineContext)
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
        cancelAllChildren()
    }

    /**
     * Triggers use case to perform refresh operation
     */
    override suspend fun refresh() {
        val uiModel = state.value
        uiModel?.refresh?.invoke()
    }

    /**
     * Triggers use case to perform a retry operation
     */
    override suspend fun retry() {
        val uiModel = state.value
        uiModel?.retry?.invoke()
    }
}
