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

package co.anitrend.media.carousel.component.viewmodel.state

import co.anitrend.core.component.viewmodel.state.AniTrendViewModelState
import co.anitrend.data.carousel.GetCarouselInteractor
import co.anitrend.domain.carousel.entity.MediaCarousel
import co.anitrend.domain.carousel.model.CarouselParam
import co.anitrend.navigation.MediaCarouselRouter

data class CarouselState(
    private val interactor: GetCarouselInteractor
) : AniTrendViewModelState<List<MediaCarousel>>() {

    suspend operator fun invoke(param: MediaCarouselRouter.MediaCarouselRouterParam) {
        val input = CarouselParam.Find(
            season = param.season,
            seasonYear = param.seasonYear,
            nextSeasonYear = param.nextSeasonYear,
            nextSeason = param.nextSeason,
            currentTime = param.currentTime,
            pageSize = param.pageSize,
        )
        val result = interactor(input)
        state.postValue(result)
    }
}
