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

import co.anitrend.core.component.viewmodel.state.AniTrendViewModelState
import co.anitrend.data.media.GetMediaStudiosInteractor
import co.anitrend.domain.common.sort.order.SortOrder
import co.anitrend.domain.media.entity.MediaStudioEntry
import co.anitrend.domain.media.model.MediaParam
import co.anitrend.domain.studio.enums.StudioSort
import co.anitrend.navigation.model.sorting.Sorting

class MediaStudiosViewModel(
    private val interactor: GetMediaStudiosInteractor,
) : AniTrendViewModelState<List<MediaStudioEntry>>() {
    operator fun invoke(mediaId: Long) {
        val result =
            interactor(
                MediaParam.Studios(
                    id = mediaId,
                    sort =
                        listOf(
                            Sorting(StudioSort.NAME, SortOrder.ASC),
                        ),
                ),
            )
        state.postValue(result)
    }
}
