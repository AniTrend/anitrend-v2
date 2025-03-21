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
package co.anitrend.data.feed.episode

import androidx.paging.PagedList
import co.anitrend.arch.data.state.DataState
import co.anitrend.data.android.controller.core.DefaultController
import co.anitrend.data.feed.episode.entity.EpisodeEntity
import co.anitrend.data.feed.episode.model.page.EpisodePageModel
import co.anitrend.domain.episode.entity.Episode
import co.anitrend.domain.episode.interactor.EpisodeUseCase
import co.anitrend.domain.episode.repository.IEpisodeRepository

internal typealias EpisodePagedController = DefaultController<EpisodePageModel, List<EpisodeEntity>>
internal typealias EpisodePagedRepository = IEpisodeRepository.Paged<DataState<PagedList<Episode>>>
internal typealias EpisodeDetailRepository = IEpisodeRepository.Detail<DataState<Episode>>
internal typealias EpisodeSyncRepository = IEpisodeRepository.Sync<DataState<Boolean>>

typealias EpisodePagedInteractor = EpisodeUseCase.Paged<DataState<PagedList<Episode>>>
typealias EpisodeDetailInteractor = EpisodeUseCase.Detail<DataState<Episode>>
typealias EpisodeSyncInteractor = EpisodeUseCase.Sync<DataState<Boolean>>
