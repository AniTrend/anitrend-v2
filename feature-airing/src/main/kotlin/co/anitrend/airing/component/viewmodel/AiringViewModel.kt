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

package co.anitrend.airing.component.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import co.anitrend.airing.component.viewmodel.state.AiringState
import co.anitrend.arch.extension.ext.extra
import co.anitrend.core.component.viewmodel.AniTrendViewModel
import co.anitrend.navigation.AiringRouter

class AiringViewModel(
    val state: AiringState,
    stateHandle: SavedStateHandle
) : AniTrendViewModel(state) {

    val param: AiringRouter.Param? by stateHandle.extra(AiringRouter.Param.KEY)

    val filter = MutableLiveData<AiringRouter.Param>(param)
}