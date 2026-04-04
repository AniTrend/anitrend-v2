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
import co.anitrend.data.media.GetMediaRecommendationsInteractor
import co.anitrend.domain.media.entity.MediaRecommendationEntry
import co.anitrend.domain.media.model.MediaParam
import co.anitrend.domain.recommendation.enums.RecommendationSort

class MediaRecommendationsViewModel(
    private val interactor: GetMediaRecommendationsInteractor,
) : AniTrendViewModelState<List<MediaRecommendationEntry>>() {
    operator fun invoke(
        mediaId: Long,
        perPage: Int = 18,
    ) {
        val result =
            interactor(
                MediaParam.Recommendations(
                    id = mediaId,
                    perPage = perPage,
                    sort = listOf(RecommendationSort.RATING),
                ),
            )
        state.postValue(result)
    }
}
