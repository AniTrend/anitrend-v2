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

package co.anitrend.media.discover.filter.component.viewmodel.genre.state

import co.anitrend.core.component.viewmodel.state.AniTrendViewModelState
import co.anitrend.data.genre.GenreInteractor
import co.anitrend.domain.genre.entity.Genre
import co.anitrend.domain.genre.model.GenreParam

class GenreViewModelState(
    override val interactor: GenreInteractor
) : AniTrendViewModelState<List<Genre>>() {

    operator fun invoke(param: GenreParam) {
        val result = interactor.getMediaGenres(param)
        state.postValue(result)
    }
}