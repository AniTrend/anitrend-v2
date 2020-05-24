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

import co.anitrend.data.media.model.remote.MediaModel
import co.anitrend.data.recommendation.model.contract.IRecommendation
import co.anitrend.data.user.model.remote.User
import co.anitrend.domain.recommendation.enums.RecommendationRating

internal data class Recommendation(
    override val media: MediaModel?,
    override val mediaRecommendation: MediaModel?,
    override val rating: Int?,
    override val user: User?,
    override val userRating: RecommendationRating?,
    override val id: Long
) : IRecommendation