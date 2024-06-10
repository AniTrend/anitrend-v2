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

package co.anitrend.media.carousel.component.viewmodel

import androidx.lifecycle.viewModelScope
import co.anitrend.core.component.viewmodel.AniTrendViewModel
import co.anitrend.domain.carousel.model.CarouselParam
import co.anitrend.media.carousel.component.viewmodel.state.CarouselState
import kotlinx.coroutines.launch

class CarouselViewModel(
    override val state: CarouselState
) : AniTrendViewModel() {
    operator fun invoke(param: CarouselParam.Find) {
        viewModelScope.launch {
            state(param)
        }
    }
}
