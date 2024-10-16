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

package co.anitrend.data.carousel.repository

import co.anitrend.arch.data.state.DataState.Companion.create
import co.anitrend.data.carousel.MediaCarouselListRepository
import co.anitrend.data.carousel.source.contract.CarouselSource
import co.anitrend.domain.carousel.model.CarouselParam


internal class MediaCarouselRepository(
    private val source: CarouselSource
) : MediaCarouselListRepository {
    override suspend fun getMediaCarousel(
        param: CarouselParam.Find
    ) = source create source(param)
}
