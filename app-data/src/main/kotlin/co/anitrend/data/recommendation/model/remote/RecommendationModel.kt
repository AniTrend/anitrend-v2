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

package co.anitrend.data.recommendation.model.remote

import co.anitrend.data.media.model.MediaModelCore
import co.anitrend.data.media.model.MediaModelExtended
import co.anitrend.data.recommendation.model.contract.IRecommendationModel
import co.anitrend.data.user.model.remote.UserModelCore
import co.anitrend.domain.recommendation.enums.RecommendationRating
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RecommendationModel(
    @SerialName("media") override val media: MediaModelCore?,
    @SerialName("mediaRecommendation") override val mediaRecommendation: MediaModelExtended?,
    @SerialName("rating") override val rating: Int?,
    @SerialName("user") override val user: UserModelCore?,
    @SerialName("userRating") override val userRating: RecommendationRating?,
    @SerialName("id") override val id: Long
) : IRecommendationModel