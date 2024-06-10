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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import co.anitrend.arch.core.model.ISupportViewModelState
import co.anitrend.arch.data.state.DataState
import kotlinx.coroutines.cancel
import timber.log.Timber
import java.util.concurrent.CancellationException

abstract class AniTrendViewModelState<T> : ViewModel(), ISupportViewModelState<T> {

    protected val state = MutableLiveData<DataState<T>>()

    override val model = state.switchMap {
        Timber.i("Performing `model` switch map using ${viewModelScope.coroutineContext} on $this")
        it.model.asLiveData(viewModelScope.coroutineContext)
    }

    override val loadState = state.switchMap {
        Timber.i("Performing `loadState` switch map using ${viewModelScope.coroutineContext} on $this")
        it.loadState.asLiveData(viewModelScope.coroutineContext)
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

    override fun onCleared() {
        viewModelScope.cancel(
            cause = CancellationException("onCleared")
        )
        super.onCleared()
    }
}
