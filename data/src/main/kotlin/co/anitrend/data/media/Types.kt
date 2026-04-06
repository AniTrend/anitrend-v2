/*
 * Copyright (C) 2020 AniTrend
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
import androidx.paging.PagingData
import co.anitrend.arch.data.state.DataState
import co.anitrend.data.android.controller.graphql.GraphQLController
import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.data.media.entity.MediaStatsEntity
import co.anitrend.data.media.entity.connection.MediaCharacterConnectionEntity
import co.anitrend.data.media.entity.connection.MediaRelationConnectionEntity
import co.anitrend.data.media.entity.connection.MediaStaffConnectionEntity
import co.anitrend.data.media.model.container.MediaConnectionModelContainer
import co.anitrend.data.media.model.container.MediaModelContainer
import co.anitrend.data.media.model.container.MediaPeopleModelContainer
import co.anitrend.data.media.model.container.MediaSidecarModelContainer
import co.anitrend.data.recommendation.entity.connection.MediaRecommendationConnectionEntity
import co.anitrend.data.studio.entity.connection.MediaStudioConnectionEntity
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.MediaRecommendationEntry
import co.anitrend.domain.media.entity.MediaRelationEntry
import co.anitrend.domain.media.entity.MediaStats
import co.anitrend.domain.media.entity.MediaStudioEntry
import co.anitrend.domain.media.entity.MediaPerson
import co.anitrend.domain.media.interactor.MediaUseCase
import co.anitrend.domain.media.repository.IMediaRepository
import kotlinx.coroutines.flow.Flow

internal typealias MediaDetailController = GraphQLController<MediaModelContainer.Detail, MediaEntity>
internal typealias MediaCharactersController = GraphQLController<MediaPeopleModelContainer.Characters, List<MediaCharacterConnectionEntity>>
internal typealias MediaPagedController = GraphQLController<MediaModelContainer.Paged, List<MediaEntity>>
internal typealias MediaNetworkController = GraphQLController<MediaModelContainer.Paged, List<Media>>
internal typealias MediaRelationsController = GraphQLController<MediaConnectionModelContainer.Relations, List<MediaRelationConnectionEntity>>
internal typealias MediaRecommendationsController = GraphQLController<MediaConnectionModelContainer.Recommendations, List<MediaRecommendationConnectionEntity>>
internal typealias MediaStaffController = GraphQLController<MediaPeopleModelContainer.Staff, List<MediaStaffConnectionEntity>>
internal typealias MediaStudiosController = GraphQLController<MediaSidecarModelContainer.Studios, List<MediaStudioConnectionEntity>>
internal typealias MediaStatsController = GraphQLController<MediaSidecarModelContainer.Stats, MediaStatsEntity?>

internal typealias MediaDetailRepository = IMediaRepository.Detail<DataState<Media>>
internal typealias MediaCharactersRepository = IMediaRepository.Characters<Flow<PagingData<MediaPerson.Character>>>
internal typealias MediaPagedRepository = IMediaRepository.Paged<DataState<PagedList<Media>>>
internal typealias MediaNetworkRepository = IMediaRepository.Network<DataState<PagedList<Media>>>
internal typealias MediaRelationsRepository = IMediaRepository.Relations<DataState<List<MediaRelationEntry>>>
internal typealias MediaRecommendationsRepository = IMediaRepository.Recommendations<DataState<List<MediaRecommendationEntry>>>
internal typealias MediaStaffRepository = IMediaRepository.Staff<Flow<PagingData<MediaPerson.Staff>>>
internal typealias MediaStudiosRepository = IMediaRepository.Studios<DataState<List<MediaStudioEntry>>>
internal typealias MediaStatsRepository = IMediaRepository.Stats<DataState<MediaStats>>

typealias GetDetailMediaInteractor = MediaUseCase.GetDetail<DataState<Media>>
typealias GetMediaCharactersInteractor = MediaUseCase.GetCharacters<Flow<PagingData<MediaPerson.Character>>>
typealias GetPagedMediaInteractor = MediaUseCase.GetPaged<DataState<PagedList<Media>>>
typealias GetNetworkMediaInteractor = MediaUseCase.GetByNetwork<DataState<PagedList<Media>>>
typealias GetMediaRelationsInteractor = MediaUseCase.GetRelations<DataState<List<MediaRelationEntry>>>
typealias GetMediaRecommendationsInteractor = MediaUseCase.GetRecommendations<DataState<List<MediaRecommendationEntry>>>
typealias GetMediaStaffInteractor = MediaUseCase.GetStaff<Flow<PagingData<MediaPerson.Staff>>>
typealias GetMediaStudiosInteractor = MediaUseCase.GetStudios<DataState<List<MediaStudioEntry>>>
typealias GetMediaStatsInteractor = MediaUseCase.GetStats<DataState<MediaStats>>
