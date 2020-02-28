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

import co.anitrend.data.media.model.remote.Media
import co.anitrend.data.user.model.remote.User
import co.anitrend.domain.common.entity.IEntity

/**
 * The media the recommendation is from
 * The recommended media
 * Users rating of the recommendation
 * The user that first created the recommendation
 * The rating of the recommendation by currently authenticated user
 */
internal interface IRecommendation : IEntity {
    val media: Media?
    val mediaRecommendation: Media?
    val rating: Int?
    val user: User?
    val userRating: RecommendationRating?
}