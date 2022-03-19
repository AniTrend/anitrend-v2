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

package co.anitrend.medialist.component.container.viewmodel

import androidx.lifecycle.*
import co.anitrend.arch.extension.ext.extra
import co.anitrend.domain.user.entity.User
import co.anitrend.medialist.component.container.viewmodel.state.UserState
import co.anitrend.navigation.MediaListRouter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class UserViewModel(
    val state: UserState,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    init {
        state.context = viewModelScope.coroutineContext
        viewModelScope.launch {
            savedStateHandle.getLiveData<MediaListRouter.Param>(
                MediaListRouter.Param.KEY
            ).asFlow().collect(state::invoke)
        }
    }

    val param: MediaListRouter.Param? by savedStateHandle.extra(MediaListRouter.Param.KEY)

    val tabConfigurationListInfo = Transformations.map(state.model) {
        val user = it as User.Extended
        user.mediaListInfo.filter { mediaListInfo ->
            mediaListInfo.mediaType == requireNotNull(param?.type)
        }
    }

    /**
     * This method will be called when this ViewModel is no longer used and will be destroyed.
     *
     *
     * It is useful when ViewModel observes some data and you need to clear this subscription to
     * prevent a leak of this ViewModel.
     */
    override fun onCleared() {
        state.onCleared()
        super.onCleared()
    }
}