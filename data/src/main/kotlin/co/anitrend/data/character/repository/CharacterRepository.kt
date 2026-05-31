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
package co.anitrend.data.character.repository

import androidx.paging.PagingData
import co.anitrend.data.character.CharacterSearchRepository
import co.anitrend.data.character.source.contract.CharacterSource
import co.anitrend.domain.character.entity.Character
import co.anitrend.domain.character.model.CharacterParam
import kotlinx.coroutines.flow.Flow

internal sealed class CharacterRepository {
    class Search(
        private val source: CharacterSource.Search,
    ) : CharacterRepository(),
        CharacterSearchRepository {
        override fun getSearch(param: CharacterParam.Find): Flow<PagingData<Character>> = source(param)
    }
}
