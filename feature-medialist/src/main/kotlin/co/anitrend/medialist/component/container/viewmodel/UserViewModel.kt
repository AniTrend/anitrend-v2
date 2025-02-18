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
package co.anitrend.medialist.component.container.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import co.anitrend.arch.extension.ext.extra
import co.anitrend.core.component.viewmodel.state.AniTrendViewModelState
import co.anitrend.data.user.GetProfileInteractor
import co.anitrend.domain.user.entity.User
import co.anitrend.domain.user.entity.attribute.MediaListInfo
import co.anitrend.domain.user.model.UserParam
import co.anitrend.navigation.MediaListRouter
import co.anitrend.navigation.extensions.nameOf
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class UserViewModel(
    private val interactor: GetProfileInteractor,
    private val savedStateHandle: SavedStateHandle,
) : AniTrendViewModelState<User>() {
    val param by savedStateHandle.extra<MediaListRouter.MediaListParam>(
        key = nameOf<MediaListRouter.MediaListParam>(),
    )

    val tabConfigurationListInfo: LiveData<List<MediaListInfo>> =
        model.map {
            val user = it as User.Extended
            user.mediaListInfo.filter { mediaListInfo ->
                mediaListInfo.mediaType == requireNotNull(param?.type)
            }
        }

    init {
        viewModelScope.launch {
            savedStateHandle
                .getStateFlow<MediaListRouter.MediaListParam?>(
                    key = nameOf<MediaListRouter.MediaListParam>(),
                    initialValue = null,
                ).filterNotNull()
                .collect { invoke(it) }
        }
    }

    operator fun invoke(arg: MediaListRouter.MediaListParam) {
        viewModelScope.launch {
            val query =
                UserParam.Profile(
                    id = arg.userId,
                    name = arg.userName,
                )
            val result = interactor(query)
            state.postValue(result)
        }
    }
}
