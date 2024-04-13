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

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import co.anitrend.arch.extension.ext.extra
import co.anitrend.core.component.viewmodel.AniTrendViewModel
import co.anitrend.domain.user.entity.User
import co.anitrend.medialist.component.container.viewmodel.state.UserState
import co.anitrend.navigation.MediaListRouter
import kotlinx.coroutines.launch

class UserViewModel(
    val state: UserState,
    private val savedStateHandle: SavedStateHandle,
) : AniTrendViewModel(state) {

    init {
        viewModelScope.launch {
            savedStateHandle.getLiveData<MediaListRouter.MediaListParam>(
                MediaListRouter.MediaListParam.KEY
            ).asFlow().collect(state::invoke)
        }
    }

    val param: MediaListRouter.MediaListParam? by savedStateHandle.extra(MediaListRouter.MediaListParam.KEY)

    val tabConfigurationListInfo = state.model.map {
        val user = it as User.Extended
        user.mediaListInfo.filter { mediaListInfo ->
            mediaListInfo.mediaType == requireNotNull(param?.type)
        }
    }
}
