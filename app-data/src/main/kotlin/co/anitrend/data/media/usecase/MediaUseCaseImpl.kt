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

package co.anitrend.data.media.usecase

import androidx.paging.PagedList
import co.anitrend.arch.data.repository.SupportRepository
import co.anitrend.arch.data.state.DataState
import co.anitrend.data.media.repository.MediaCarouselRepositoryImpl
import co.anitrend.data.media.repository.MediaRepositoryImpl
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.MediaCarousel
import co.anitrend.domain.media.interactor.MediaCarouselUseCase
import co.anitrend.domain.media.interactor.MediaUseCase

internal class MediaUseCaseImpl(
    repository: MediaRepositoryImpl
) : MediaUseCaseContract(repository) {

    /**
     * Informs underlying repositories or related components running background operations to stop
     */
    override fun onCleared() {
        repository as SupportRepository
        repository.onCleared()
    }
}

typealias MediaUseCaseContract = MediaUseCase<DataState<PagedList<Media>>>

internal class MediaCarouselUseCaseImpl(
    repository: MediaCarouselRepositoryImpl
) : MediaCarouselUseCaseContract(repository) {

    /**
     * Informs underlying repositories or related components running background operations to stop
     */
    override fun onCleared() {
        repository as SupportRepository
        repository.onCleared()
    }
}

typealias MediaCarouselUseCaseContract = MediaCarouselUseCase<DataState<List<MediaCarousel>>>