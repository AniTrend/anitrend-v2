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

package co.anitrend.data.thread.model.remote

import co.anitrend.data.media.model.MediaModelCore
import co.anitrend.data.media.model.MediaModelExtended
import co.anitrend.data.shared.common.Identity
import co.anitrend.data.thread.model.contract.IThreadModel
import co.anitrend.data.user.model.remote.UserModelCore

/** [Thread](Notification](https://anilist.github.io/ApiV2-GraphQL-Docs/thread.doc.html)
 * Forum Thread
 *
 */
internal data class ThreadModel(
    override val id: Long,
    override val body: String?,
    override val categories: List<ThreadCategory>?,
    override val createdAt: Long,
    override val isLocked: Boolean,
    override val isSticky: Boolean,
    override val isSubscribed: Boolean,
    override val likes: List<UserModelCore>?,
    override val mediaCategories: List<MediaModelCore>?,
    override val repliedAt: Long?,
    override val replyCommentId: Int?,
    override val replyCount: Int?,
    override val replyUser: UserModelCore?,
    override val replyUserId: Long?,
    override val siteUrl: String,
    override val title: String,
    override val updatedAt: Long,
    override val user: UserModelCore,
    override val userId: Long,
    override val viewCount: Int
) : IThreadModel {

    /** [ThreadCategory](https://anilist.github.io/ApiV2-GraphQL-Docs/threadcategory.doc.html)
     * A forum thread category
     */
    internal data class ThreadCategory(
        val name: String,
        override val id: Long,
    ) : Identity
}