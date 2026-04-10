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
import co.anitrend.data.review.GetPagingReviewInteractor
import co.anitrend.domain.common.sort.order.SortOrder
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.domain.review.entity.Review
import co.anitrend.domain.review.enums.ReviewSort
import co.anitrend.domain.review.model.ReviewParam
import co.anitrend.navigation.model.sorting.Sorting
import kotlinx.coroutines.flow.Flow

class MediaCommunityViewModel(
    private val interactor: GetPagingReviewInteractor,
) : ViewModel() {
    companion object {
        val previewSort: List<Sorting<ReviewSort>> =
            listOf(
                Sorting(ReviewSort.RATING, SortOrder.DESC),
                Sorting(ReviewSort.CREATED_AT, SortOrder.DESC),
            )
    }

    fun reviews(
        mediaId: Long,
        mediaType: MediaType,
        scoreFormat: ScoreFormat,
    ): Flow<PagingData<Review>> =
        interactor(
            ReviewParam.Paged(
                mediaId = mediaId,
                mediaType = mediaType,
                scoreFormat = scoreFormat,
                sort = previewSort,
            ),
        ).cachedIn(viewModelScope)
}
