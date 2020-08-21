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

package co.anitrend.data.recommendation.model.contract

import co.anitrend.data.media.model.contract.IMedia
import co.anitrend.data.user.model.contract.IUser
import co.anitrend.data.shared.common.Identity
import co.anitrend.domain.recommendation.enums.RecommendationRating

/**
 * A recommendation on another media type
 *
 * @property media The media the recommendation is from
 * @property mediaRecommendation The recommended media
 * @property rating Users rating of the recommendation
 * @property user The user that first created the recommendation
 * @property userRating The rating of the recommendation by currently authenticated user
 */
internal interface IRecommendation : Identity {
    val media: IMedia?
    val mediaRecommendation: IMedia?
    val rating: Int?
    val user: IUser?
    val userRating: RecommendationRating?
}