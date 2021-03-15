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

package co.anitrend.data.media

import androidx.paging.PagedList
import co.anitrend.arch.data.state.DataState
import co.anitrend.data.arch.controller.graphql.GraphQLController
import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.data.media.model.container.MediaModelContainer
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.interactor.MediaUseCase
import co.anitrend.domain.media.repository.IMediaRepository

internal typealias MediaDetailController = GraphQLController<MediaModelContainer.Detail, MediaEntity>
internal typealias MediaPagedController = GraphQLController<MediaModelContainer.Paged, List<MediaEntity>>
internal typealias MediaNetworkController = GraphQLController<MediaModelContainer.Paged, List<Media>>

internal typealias MediaDetailRepository = IMediaRepository.Detail<DataState<Media>>
internal typealias MediaPagedRepository = IMediaRepository.Paged<DataState<PagedList<Media>>>
internal typealias MediaNetworkRepository = IMediaRepository.Network<DataState<PagedList<Media>>>

typealias GetDetailMediaInteractor = MediaUseCase.GetDetail<DataState<Media>>
typealias GetPagedMediaInteractor = MediaUseCase.GetPaged<DataState<PagedList<Media>>>
typealias GetNetworkMediaInteractor = MediaUseCase.GetByNetwork<DataState<PagedList<Media>>>