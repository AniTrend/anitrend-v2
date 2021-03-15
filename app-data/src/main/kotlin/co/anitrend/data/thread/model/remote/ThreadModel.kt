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

import co.anitrend.data.media.model.MediaModel
import co.anitrend.data.shared.common.Identity
import co.anitrend.data.thread.model.contract.IThreadModel
import co.anitrend.data.user.model.UserModel

/** [Thread](Notification](https://anilist.github.io/ApiV2-GraphQL-Docs/thread.doc.html)
 * Forum Thread
 *
 *
 * @property categories The categories of the thread
 *
 */
internal sealed class ThreadModel : IThreadModel {

    abstract val categories: List<Category>?

    /** [ThreadCategory](https://anilist.github.io/ApiV2-GraphQL-Docs/threadcategory.doc.html)
     * A forum thread category
     */
    internal data class Category(
        val name: String,
        override val id: Long,
    ) : Identity

    internal data class Core(
        override val id: Long,
        override val body: String?,
        override val categories: List<Category>?,
        override val createdAt: Long,
        override val isLiked: Boolean = false,
        override val isLocked: Boolean = false,
        override val isSticky: Boolean = false,
        override val isSubscribed: Boolean = false,
        override val likeCount: Int,
        override val repliedAt: Long?,
        override val replyCommentId: Int?,
        override val replyCount: Int?,
        override val replyUser: UserModel?,
        override val replyUserId: Long?,
        override val siteUrl: String,
        override val title: String,
        override val updatedAt: Long,
        override val user: UserModel.Core,
        override val userId: Long,
        override val viewCount: Int
    ) : ThreadModel()

    /**
     *
     * @param likes The users who liked the thread
     * @param mediaCategories The media categories of the thread
     */
    internal data class Extended(
        val likes: List<UserModel.Core>?,
        val mediaCategories: List<MediaModel.Core>?,
        override val body: String?,
        override val categories: List<Category>?,
        override val createdAt: Long,
        override val isLiked: Boolean = false,
        override val isLocked: Boolean = false,
        override val isSticky: Boolean = false,
        override val isSubscribed: Boolean = false,
        override val likeCount: Int,
        override val repliedAt: Long?,
        override val replyCommentId: Int?,
        override val replyCount: Int?,
        override val replyUser: UserModel?,
        override val replyUserId: Long?,
        override val siteUrl: String,
        override val title: String,
        override val updatedAt: Long,
        override val user: UserModel.Core,
        override val userId: Long,
        override val viewCount: Int,
        override val id: Long
    ) : ThreadModel()
}