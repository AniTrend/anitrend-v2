/*
 * Copyright (C) 2019  AniTrend
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

package co.anitrend.data.genre.usecase

import co.anitrend.arch.data.model.UserInterfaceState
import co.anitrend.data.media.model.remote.MediaGenre
import co.anitrend.data.genre.repository.MediaGenreRepository
import co.anitrend.domain.genre.entities.Genre
import co.anitrend.domain.genre.interactors.MediaGenreUseCase

class MediaGenreUseCaseImpl(
    private val repository: MediaGenreRepository
) : MediaGenreUseCase<UserInterfaceState<List<Genre>>>(repository) {

    /**
     * Informs underlying repositories or related components running background operations to stop
     */
    override fun onCleared() {
        repository.onCleared()
    }
}