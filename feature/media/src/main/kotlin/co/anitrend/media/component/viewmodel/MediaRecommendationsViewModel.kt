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

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import co.anitrend.core.component.viewmodel.state.AniTrendViewModelState
import co.anitrend.data.media.GetMediaRecommendationsPagingInteractor
import co.anitrend.data.media.GetMediaRecommendationsInteractor
import co.anitrend.domain.media.entity.MediaRecommendationEntry
import co.anitrend.domain.media.model.MediaParam
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.domain.recommendation.enums.RecommendationSort
import kotlinx.coroutines.flow.Flow

class MediaRecommendationsViewModel(
    private val interactor: GetMediaRecommendationsInteractor,
    private val pagingInteractor: GetMediaRecommendationsPagingInteractor,
) : AniTrendViewModelState<List<MediaRecommendationEntry>>() {
    fun recommendations(
        mediaId: Long,
        perPage: Int = 24,
        scoreFormat: ScoreFormat,
    ): Flow<PagingData<MediaRecommendationEntry>> =
        pagingInteractor(
            MediaParam.Recommendations(
                id = mediaId,
                perPage = perPage,
                sort = listOf(RecommendationSort.RATING),
                scoreFormat = scoreFormat,
            ),
        ).cachedIn(viewModelScope)

    operator fun invoke(
        mediaId: Long,
        perPage: Int = 18,
        scoreFormat: ScoreFormat,
    ) {
        val result =
            interactor(
                MediaParam.Recommendations(
                    id = mediaId,
                    perPage = perPage,
                    sort = listOf(RecommendationSort.RATING),
                    scoreFormat = scoreFormat,
                ),
            )
        state.postValue(result)
    }
}
