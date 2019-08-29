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

package co.anitrend.data.model.response.core.thread

import co.anitrend.data.model.response.core.media.Media
import co.anitrend.data.model.response.core.thread.contract.IThread
import co.anitrend.data.model.response.core.user.User

/** [Thread](Notification](https://anilist.github.io/ApiV2-GraphQL-Docs/thread.doc.html)
 * Forum Thread
 *
 */
data class Thread(
    override val id: Long,
    override val body: String?,
    override val categories: List<ThreadCategory>?,
    override val createdAt: Long,
    override val isLocked: Boolean,
    override val isSticky: Boolean,
    override val isSubscribed: Boolean,
    override val likes: List<User>?,
    override val mediaCategories: List<Media>?,
    override val repliedAt: Long?,
    override val replyCommentId: Int?,
    override val replyCount: Int?,
    override val replyUser: User?,
    override val replyUserId: Long?,
    override val siteUrl: String,
    override val title: String,
    override val updatedAt: Long,
    override val user: User,
    override val userId: Long,
    override val viewCount: Int
) : IThread