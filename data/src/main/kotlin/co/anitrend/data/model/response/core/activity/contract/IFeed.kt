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

package co.anitrend.data.model.response.core.activity.contract

import co.anitrend.data.entity.contract.IEntity
import co.anitrend.data.model.response.core.user.contract.IUser
import co.anitrend.data.usecase.activity.attributes.ActivityType

/** [Activity](https://anilist.github.io/ApiV2-GraphQL-Docs/activityunion.doc.html)
 * Activity contract with shared objects
 *
 * @property createdAt The time the activity was created at
 * @property likes The users who liked the activity
 * @property replyCount The number of activity replies
 * @property siteUrl The url for the activity page on the AniList website
 * @property text The status text (Markdown)
 * @property type The type of activity
 * @property user The user who created the activity
 * @property userId The user id of the activity's creator
 */
interface IFeed : IEntity {
    val createdAt: Long
    val likes: List<IUser>?
    val replyCount: Int
    val siteUrl: String?
    val text: String?
    val type: ActivityType?
    val user: IUser?
    val userId: Long?
}