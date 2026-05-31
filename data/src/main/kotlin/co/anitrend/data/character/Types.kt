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
package co.anitrend.data.character

import androidx.paging.PagingData
import co.anitrend.data.android.controller.graphql.GraphQLController
import co.anitrend.data.character.entity.CharacterEntity
import co.anitrend.data.character.model.container.CharacterContainer
import co.anitrend.domain.character.entity.Character
import co.anitrend.domain.character.interactor.CharacterUseCase
import co.anitrend.domain.character.repository.ICharacterRepository
import kotlinx.coroutines.flow.Flow

internal typealias CharacterPagedController = GraphQLController<CharacterContainer.Paged, List<CharacterEntity>>

internal typealias CharacterSearchRepository = ICharacterRepository.Search<Flow<PagingData<Character>>>

typealias GetSearchCharacterInteractor = CharacterUseCase.GetSearch<Flow<PagingData<Character>>>
