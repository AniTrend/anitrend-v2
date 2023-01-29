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

package co.anitrend.feed.component.viewmodel.state

import co.anitrend.arch.domain.common.IUseCase
import co.anitrend.core.component.viewmodel.state.AniTrendViewModelState

class FeedState(
    override val interactor: IUseCase
): AniTrendViewModelState<Any>() {
    
    operator fun invoke() {
        // val result = interactor()
        // state.postValue(result)
    }
}