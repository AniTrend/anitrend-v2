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

internal data class RecommendationModel(
    @SerializedName("media") override val media: MediaModelCore?,
    @SerializedName("mediaRecommendation") override val mediaRecommendation: MediaModelExtended?,
    @SerializedName("rating") override val rating: Int?,
    @SerializedName("user") override val user: UserModelCore?,
    @SerializedName("userRating") override val userRating: RecommendationRating?,
    @SerializedName("id") override val id: Long
) : IRecommendationModel