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
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.medialist.entity.MediaList
import co.anitrend.domain.medialist.interactor.MediaListUseCase
import co.anitrend.domain.medialist.repository.IMediaListRepository

internal typealias MediaListEntryController = GraphQLController<MediaListContainerModel.Entry, MediaListEntity>
internal typealias MediaListPagedController = GraphQLController<MediaListContainerModel.Paged, List<MediaListEntity>>
internal typealias MediaListCollectionController = GraphQLController<MediaListContainerModel.Collection, List<MediaListEntity>>
internal typealias MediaListSaveEntryController = GraphQLController<MediaListContainerModel.SavedEntry, MediaListEntity>
internal typealias MediaListSaveEntriesController = GraphQLController<MediaListContainerModel.SavedEntries, List<MediaListEntity>>
internal typealias MediaListDeleteEntryController = GraphQLController<MediaListContainerModel.DeletedEntry, Boolean>
internal typealias DeleteCustomListController = GraphQLController<MediaListContainerModel.DeletedCustomList, Boolean>

internal typealias MediaListSyncRepository = IMediaListRepository.Sync<DataState<Boolean>>
internal typealias MediaListEntryRepository = IMediaListRepository.Entry<DataState<Media>>
internal typealias MediaListCollectionRepository = IMediaListRepository.Collection<DataState<PagedList<Media>>>
internal typealias MediaListPagedRepository = IMediaListRepository.Paged<DataState<PagedList<Media>>>
internal typealias MediaListSaveEntryRepository = IMediaListRepository.SaveEntry<DataState<Boolean>>
internal typealias MediaListSaveEntriesRepository = IMediaListRepository.SaveEntries<DataState<Boolean>>
internal typealias MediaListDeleteEntryRepository = IMediaListRepository.DeleteEntry<DataState<Boolean>>
internal typealias DeleteCustomMediaListRepository = IMediaListRepository.DeleteCustomList<DataState<Boolean>>

typealias SyncMediaListEntryInteractor = MediaListUseCase.Sync<DataState<Boolean>>
typealias GetMediaListEntryInteractor = MediaListUseCase.GetEntry<DataState<Media>>
typealias GetPagedMediaListInteractor = MediaListUseCase.GetPaged<DataState<PagedList<Media>>>
typealias GetCollectionMediaListInteractor = MediaListUseCase.GetCollection<DataState<PagedList<Media>>>
typealias SaveMediaListEntryInteractor = MediaListUseCase.SaveEntry<DataState<Boolean>>
typealias SaveMediaListEntriesInteractor = MediaListUseCase.SaveEntries<DataState<Boolean>>
typealias DeleteMediaListEntryInteractor = MediaListUseCase.DeleteEntry<DataState<Boolean>>
typealias DeleteCustomMediaListInteractor = MediaListUseCase.DeleteCustomList<DataState<Boolean>>