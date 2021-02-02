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
import co.anitrend.data.arch.controller.graphql.GraphQLController
import co.anitrend.data.media.model.query.MediaQuery
import co.anitrend.data.medialist.entity.MediaListEntity
import co.anitrend.data.medialist.model.collection.MediaListCollectionModel
import co.anitrend.data.medialist.model.page.MediaListPageModel
import co.anitrend.data.medialist.model.query.MediaListQuery
import co.anitrend.domain.medialist.entity.MediaList
import co.anitrend.domain.medialist.interactor.MediaListUseCase
import co.anitrend.domain.medialist.repository.MediaListRepository


internal typealias MediaListPagedController = GraphQLController<MediaListPageModel, List<MediaListEntity>>
internal typealias MediaListCollectionController = GraphQLController<MediaListCollectionModel, List<MediaListEntity>>

internal typealias MediaListCollectionRepository = MediaListRepository.Collection<DataState<PagedList<MediaList>>>
internal typealias MediaListPagedRepository = MediaListRepository.Paged<DataState<PagedList<MediaList>>>
internal typealias MediaListDeleteRepository = MediaListRepository.Delete<DataState<MediaList>>
internal typealias MediaListSaveRepository = MediaListRepository.Save<DataState<MediaList>>

typealias GetMediaListPagedInteractor = MediaListUseCase.GetPaged<DataState<PagedList<MediaList>>, MediaListQuery.Paged>
typealias GetMediaListCollectionInteractor = MediaListUseCase.GetCollection<DataState<PagedList<MediaList>>, MediaListQuery.Collection>