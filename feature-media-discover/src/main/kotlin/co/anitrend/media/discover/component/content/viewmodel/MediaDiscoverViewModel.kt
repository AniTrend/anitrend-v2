/*
 * Copyright (C) 2020  AniTrend
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

package co.anitrend.media.discover.component.content.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import co.anitrend.arch.extension.ext.extra
import co.anitrend.core.component.viewmodel.AniTrendViewModel
import co.anitrend.media.discover.component.content.viewmodel.state.MediaDiscoverState
import co.anitrend.navigation.MediaDiscoverFilterRouter
import co.anitrend.navigation.MediaDiscoverRouter
import kotlinx.coroutines.launch

class MediaDiscoverViewModel(
    val state: MediaDiscoverState,
    private val savedStateHandle: SavedStateHandle,
) : AniTrendViewModel(state) {

    init {
        viewModelScope.launch {
            savedStateHandle.getLiveData<MediaDiscoverRouter.Param>(
                MediaDiscoverFilterRouter.Action.KEY
            ).asFlow().collect { param ->
                state(param)
            }
        }
    }

    val default by savedStateHandle.extra(
        MediaDiscoverRouter.Param.KEY,
        MediaDiscoverRouter.Param()
    )

    val filter = MutableLiveData(default)

    /**
     * Handle param changes by settings the new [param] to the [state]
     */
    fun setParam(param: MediaDiscoverRouter.Param) {
        savedStateHandle[MediaDiscoverFilterRouter.Action.KEY] = param
    }
}
