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

package co.anitrend.data.carousel

import co.anitrend.arch.data.state.DataState
import co.anitrend.data.android.controller.graphql.GraphQLController
import co.anitrend.data.carousel.model.CarouselModel
import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.domain.carousel.entity.MediaCarousel
import co.anitrend.domain.carousel.interactor.MediaCarouselUseCase
import co.anitrend.domain.carousel.repository.IMediaCarouselRepository

internal typealias MediaCarouselListController = GraphQLController<CarouselModel, List<MediaEntity>>

internal typealias MediaCarouselListRepository = IMediaCarouselRepository<DataState<List<MediaCarousel>>>

typealias GetCarouselInteractor = MediaCarouselUseCase<DataState<List<MediaCarousel>>>