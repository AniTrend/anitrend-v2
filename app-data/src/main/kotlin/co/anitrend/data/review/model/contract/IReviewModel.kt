/*
 * Copyright (C) 2019  AniTrend
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

package co.anitrend.data.review.model.contract

import co.anitrend.data.shared.common.Identity
import co.anitrend.data.user.model.contract.IUserModel
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.review.enums.ReviewRating

/** [Review](Notification](https://anilist.github.io/ApiV2-GraphQL-Docs/review.doc.html)
 * Review contract
 *
 * @property body The main review body text
 * @property createdAt The time of the thread creation
 * @property mediaId The id of the review's media
 * @property mediaType For which type of media the review is for
 * @property private If the review is not yet publicly published and is only viewable by creator
 * @property rating The total user rating of the review
 * @property ratingAmount The amount of user ratings of the review
 * @property score The review score of the media
 * @property siteUrl The url for the review page on the AniList website
 * @property summary A short summary of the review
 * @property updatedAt The time of the thread last update
 * @property user The creator of the review
 * @property userId The id of the review's creator
 * @property userRating The rating of the review by currently authenticated user
 */
internal interface IReviewModel : Identity {
    val body: String?
    val createdAt: Long
    val mediaId: Long
    val mediaType: MediaType?
    val private: Boolean?
    val rating: Int?
    val ratingAmount: Int?
    val score: Int?
    val siteUrl: String?
    val summary: String?
    val updatedAt: Long
    val user: IUserModel?
    val userId: Long
    val userRating: ReviewRating?
}