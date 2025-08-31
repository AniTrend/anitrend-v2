/*
 * Copyright (C) 2025 AniTrend
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

import androidx.paging.PagedList
import co.anitrend.arch.data.state.DataState
import co.anitrend.core.component.viewmodel.state.AniTrendViewModelState
import co.anitrend.domain.airing.enums.AiringSort
import co.anitrend.domain.airing.interactor.AiringScheduleUseCase
import co.anitrend.domain.airing.model.AiringParam
import co.anitrend.domain.common.sort.order.SortOrder
import co.anitrend.domain.media.entity.Media
import co.anitrend.navigation.model.sorting.Sorting
import org.threeten.bp.Instant

/**
 * ViewModel that exposes upcoming airing schedule for a specific media id.
 */
class MediaScheduleViewModel(
    private val interactor: AiringScheduleUseCase.GetPaged<DataState<PagedList<Media>>>,
) : AniTrendViewModelState<PagedList<Media>>() {
    /**
     * Load upcoming schedule for provided [mediaId]
     */
    operator fun invoke(mediaId: Long) {
        val nowEpochSec = Instant.now().epochSecond.toInt()
        val param =
            AiringParam.Find(
                mediaId = mediaId,
                airingAt_greater = nowEpochSec,
                notYetAired = true,
                sort = listOf(Sorting(AiringSort.TIME, SortOrder.ASC)),
            )
        val result = interactor(param)
        state.postValue(result)
    }
}
