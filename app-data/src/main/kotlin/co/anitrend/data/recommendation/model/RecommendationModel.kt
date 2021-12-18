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

package co.anitrend.data.recommendation.model

import co.anitrend.data.media.model.MediaModel
import co.anitrend.data.recommendation.model.contract.IRecommendationModel
import co.anitrend.data.user.model.UserModel
import co.anitrend.domain.recommendation.enums.RecommendationRating
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal sealed class RecommendationModel : IRecommendationModel {

    @Serializable
    internal data class Core(
        @SerialName("rating") override val rating: Int?,
        @SerialName("user") override val user: UserModel.Core?,
        @SerialName("media") override val media: MediaModel.Core?,
        @SerialName("userRating") override val userRating: RecommendationRating?,
        @SerialName("mediaRecommendation") override val mediaRecommendation: MediaModel.Core?,
        @SerialName("id") override val id: Long
    ) : RecommendationModel()
}