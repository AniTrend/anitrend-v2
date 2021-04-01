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

package co.anitrend.data.medialist

import androidx.paging.PagedList
import co.anitrend.arch.data.state.DataState
import co.anitrend.data.android.controller.graphql.GraphQLController
import co.anitrend.data.medialist.entity.MediaListEntity
import co.anitrend.data.medialist.model.container.MediaListContainerModel
import co.anitrend.domain.medialist.entity.MediaList
import co.anitrend.domain.medialist.interactor.MediaListUseCase
import co.anitrend.domain.medialist.repository.IMediaListRepository

internal typealias MediaListPagedController = GraphQLController<MediaListContainerModel.Paged, List<MediaListEntity>>
internal typealias MediaListCollectionController = GraphQLController<MediaListContainerModel.Collection, List<MediaListEntity>>

internal typealias MediaListCollectionRepository = IMediaListRepository.Collection<DataState<PagedList<MediaList>>>
internal typealias MediaListPagedRepository = IMediaListRepository.Paged<DataState<PagedList<MediaList>>>
internal typealias MediaListDeleteRepository = IMediaListRepository.Delete<DataState<MediaList>>
internal typealias MediaListSaveRepository = IMediaListRepository.Save<DataState<MediaList>>

typealias GetPagedMediaListInteractor = MediaListUseCase.GetPaged<DataState<PagedList<MediaList>>>
typealias GetCollectionMediaListInteractor = MediaListUseCase.GetCollection<DataState<PagedList<MediaList>>>