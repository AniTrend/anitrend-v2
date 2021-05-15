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

import androidx.lifecycle.LiveData
import co.anitrend.arch.core.model.ISupportViewModelState
import co.anitrend.arch.domain.entities.LoadState

class MediaListTabTypeState(

) : ISupportViewModelState<List<String>> {

    override val loadState: LiveData<LoadState>
        get() = TODO("Not yet implemented")

    override val model: LiveData<List<String>>
        get() = TODO("Not yet implemented")

    override val refreshState: LiveData<LoadState>
        get() = TODO("Not yet implemented")

    /**
     * Called upon [androidx.lifecycle.ViewModel.onCleared] and should optionally
     * call cancellation of any ongoing jobs.
     *
     * If your use case source is of type [co.anitrend.arch.domain.common.IUseCase]
     * then you could optionally call [co.anitrend.arch.domain.common.IUseCase.onCleared] here
     */
    override fun onCleared() {

    }

    /**
     * Triggers use case to perform refresh operation
     */
    override suspend fun refresh() {
        throw NotImplementedError()
    }

    /**
     * Triggers use case to perform a retry operation
     */
    override suspend fun retry() {
        throw NotImplementedError()
    }
}