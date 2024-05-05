@file:Suppress("standard:no-empty-file")
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

package co.anitrend.data.genre

import co.anitrend.arch.data.state.DataState
import co.anitrend.data.android.controller.graphql.GraphQLController
import co.anitrend.data.genre.entity.GenreEntity
import co.anitrend.data.genre.model.GenreCollection
import co.anitrend.domain.genre.entity.Genre
import co.anitrend.domain.genre.interactor.GenreUseCase
import co.anitrend.domain.genre.repository.IGenreRepository

internal typealias MediaGenreController = GraphQLController<GenreCollection, List<GenreEntity>>
internal typealias GenreListRepository = IGenreRepository<DataState<List<Genre>>>

typealias GenreInteractor = GenreUseCase<DataState<List<Genre>>>
