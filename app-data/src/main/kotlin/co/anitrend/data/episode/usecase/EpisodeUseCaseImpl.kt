/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.data.episode.usecase

import co.anitrend.arch.data.repository.contract.ISupportRepository
import co.anitrend.data.episode.EpisodeDetailInteractor
import co.anitrend.data.episode.EpisodeDetailRepository
import co.anitrend.data.episode.EpisodePagedInteractor
import co.anitrend.data.episode.EpisodePagedRepository

internal sealed class EpisodeUseCaseImpl {

    internal class Detail(
        repository: EpisodeDetailRepository
    ) : EpisodeDetailInteractor(repository) {
        /**
         * Informs underlying repositories or related components running background operations to stop
         */
        override fun onCleared() {
            repository as ISupportRepository
            repository.onCleared()
        }
    }

    internal class Paged(
        repository: EpisodePagedRepository
    ) : EpisodePagedInteractor(repository) {
        /**
         * Informs underlying repositories or related components running background operations to stop
         */
        override fun onCleared() {
            repository as ISupportRepository
            repository.onCleared()
        }
    }
}