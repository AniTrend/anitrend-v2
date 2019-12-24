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

package co.anitrend.data.activity.model.remote.common

import co.anitrend.domain.common.entity.IEntity
import co.anitrend.domain.user.entities.IUser
import co.anitrend.domain.activity.enums.ActivityType

/** [Activity](https://anilist.github.io/ApiV2-GraphQL-Docs/activityunion.doc.html)
 * Activity contract with shared objects
 *
 * @property createdAt The time the activity was created at
 * @property likes The users who liked the activity
 * @property replyCount The number of activity replies
 * @property siteUrl The url for the activity page on the AniList website
 * @property type The type of activity
 */
interface IFeedModel : IEntity {
    val createdAt: Long
    val likes: List<IUser>?
    val replyCount: Int
    val siteUrl: String?
    val type: ActivityType?
}