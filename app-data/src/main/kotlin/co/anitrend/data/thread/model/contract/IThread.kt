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

package co.anitrend.data.thread.model.contract

import co.anitrend.data.media.model.contract.IMedia
import co.anitrend.domain.common.entity.IEntity
import co.anitrend.data.user.model.contract.IUser

/** [Thread](Notification](https://anilist.github.io/ApiV2-GraphQL-Docs/thread.doc.html)
 * Forum Thread Contract
 *
 * @property body The text body of the thread (Markdown)
 * @property categories The categories of the thread
 * @property createdAt The time of the thread creation
 * @property isLocked If the thread is locked and can receive comments
 * @property isSticky If the thread is stickied and should be displayed at the top of the page
 * @property isSubscribed If the currently authenticated user is subscribed to the thread
 * @property likes The users who liked the thread
 * @property mediaCategories The media categories of the thread
 * @property repliedAt The time of the last reply
 * @property replyCommentId The id of the most recent comment on the thread
 * @property replyCount The number of comments on the thread
 * @property replyUser The user to last reply to the thread
 * @property replyUserId The id of the user who most recently commented on the thread
 * @property siteUrl The url for the thread page on the AniList website
 * @property title The title of the thread
 * @property updatedAt The time of the thread last update
 * @property user The owner of the thread
 * @property userId The id of the thread owner user
 * @property viewCount The number of times users have viewed the thread
 */
internal interface IThread : IEntity {
    val body: String?
    val categories: List<IThreadCategory>?
    val createdAt: Long
    val isLocked: Boolean
    val isSticky: Boolean
    val isSubscribed: Boolean
    val likes: List<IUser>?
    val mediaCategories: List<IMedia>?
    val repliedAt: Long?
    val replyCommentId: Int?
    val replyCount: Int?
    val replyUser: IUser?
    val replyUserId: Long?
    val siteUrl: String
    val title: String
    val updatedAt: Long
    val user: IUser
    val userId: Long
    val viewCount: Int
}