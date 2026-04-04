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

import androidx.paging.PagedList
import co.anitrend.core.component.viewmodel.state.AniTrendViewModelState
import co.anitrend.data.review.GetReviewPagedInteractor
import co.anitrend.domain.common.sort.order.SortOrder
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.domain.review.entity.Review
import co.anitrend.domain.review.enums.ReviewSort
import co.anitrend.domain.review.model.ReviewParam
import co.anitrend.navigation.model.sorting.Sorting

class MediaCommunityViewModel(
    private val interactor: GetReviewPagedInteractor,
) : AniTrendViewModelState<PagedList<Review>>() {
    operator fun invoke(
        mediaId: Long,
        mediaType: MediaType,
        scoreFormat: ScoreFormat,
    ) {
        val result =
            interactor(
                ReviewParam.Paged(
                    mediaId = mediaId,
                    mediaType = mediaType,
                    scoreFormat = scoreFormat,
                    sort =
                        listOf(
                            Sorting(ReviewSort.RATING, SortOrder.DESC),
                            Sorting(ReviewSort.CREATED_AT, SortOrder.DESC),
                        ),
                ),
            )
        state.postValue(result)
    }
}
