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

package co.anitrend.data.activity.model.remote

import co.anitrend.arch.data.mapper.contract.ISupportMapperHelper
import co.anitrend.data.activity.entity.ReplyFeedEntity
import co.anitrend.data.activity.model.remote.common.IFeed
import co.anitrend.data.user.model.remote.User
import co.anitrend.domain.activity.enums.ActivityType

/** [ActivityReply](https://anilist.github.io/ApiV2-GraphQL-Docs/textactivity.doc.html)
 * Replay to an activity item
 *
 * @param activityId The id of the parent activity
 * @property user The user who created the activity
 * @property userId The user id of the activity's creator
 * @property text The status text (Markdown)
 */
internal data class ReplyFeed(
    val activityId: Long?,
    val text: String?,
    val user: User?,
    val userId: Long?,
    override val id: Long,
    override val createdAt: Long,
    override val likes: List<User>?,
    override val siteUrl: String?,
    override val type: ActivityType?,
    override val replyCount: Int
) : IFeed {

    companion object : ISupportMapperHelper<ReplyFeed, ReplyFeedEntity> {
        /**
         * Transforms the the [source] to the target type
         */
        override fun transform(source: ReplyFeed): ReplyFeedEntity {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }
}