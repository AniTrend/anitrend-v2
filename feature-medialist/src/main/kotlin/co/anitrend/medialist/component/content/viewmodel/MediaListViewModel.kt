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

package co.anitrend.medialist.component.content.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import co.anitrend.arch.extension.ext.extra
import co.anitrend.core.component.viewmodel.AniTrendViewModel
import co.anitrend.medialist.component.content.viewmodel.state.MediaListState
import co.anitrend.navigation.MediaListRouter

class MediaListViewModel(
    val state: MediaListState,
    savedStateHandle: SavedStateHandle
) : AniTrendViewModel(state) {

    val param: MediaListRouter.MediaListParam? by savedStateHandle.extra(MediaListRouter.MediaListParam.KEY)

    val filter = MutableLiveData(param)
}
