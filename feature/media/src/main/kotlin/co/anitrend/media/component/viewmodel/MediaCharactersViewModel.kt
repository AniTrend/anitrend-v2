/*
 * Copyright (C) 2026 AniTrend
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
package co.anitrend.media.component.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import co.anitrend.data.media.GetMediaCharactersInteractor
import co.anitrend.domain.character.enums.CharacterSort
import co.anitrend.domain.common.sort.order.SortOrder
import co.anitrend.domain.media.entity.MediaPerson
import co.anitrend.domain.media.model.MediaParam
import co.anitrend.navigation.model.sorting.Sorting
import kotlinx.coroutines.flow.Flow

class MediaCharactersViewModel(
    private val interactor: GetMediaCharactersInteractor,
) : ViewModel() {
    fun characters(mediaId: Long): Flow<PagingData<MediaPerson.Character>> =
        interactor(
            MediaParam.Characters(
                id = mediaId,
                sort =
                    listOf(
                        Sorting(CharacterSort.ROLE, SortOrder.DESC),
                        Sorting(CharacterSort.RELEVANCE, SortOrder.DESC),
                    ),
            ),
        ).cachedIn(viewModelScope)
}
