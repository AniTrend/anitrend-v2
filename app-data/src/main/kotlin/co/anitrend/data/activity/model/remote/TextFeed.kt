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

import co.anitrend.data.activity.model.remote.common.IFeedModel
import co.anitrend.data.user.model.remote.UserModelCore
import co.anitrend.domain.activity.enums.ActivityType
import com.google.gson.annotations.SerializedName

/** [TextActivity](https://anilist.github.io/ApiV2-GraphQL-Docs/textactivity.doc.html)
 * User text activity
 *
 * @param replies The written replies to the activity
 * @param isLocked If the activity is locked and can receive replies
 * @property user The user who created the activity
 * @property userId The user id of the activity's creator
 */
internal data class TextFeed(
    val replies: List<ReplyFeed>?,
    val isLocked: Boolean,
    val user: UserModelCore?,
    val userId: Long?,
    val text: String?,
    @SerializedName("id") override val id: Long,
    @SerializedName("createdAt") override val createdAt: Long,
    @SerializedName("likes") override val likes: List<UserModelCore>?,
    @SerializedName("siteUrl") override val siteUrl: String?,
    @SerializedName("type") override val type: ActivityType?,
    @SerializedName("replyCount") override val replyCount: Int
) : IFeedModel