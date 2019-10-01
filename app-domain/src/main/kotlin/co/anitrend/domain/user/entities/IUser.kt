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

package co.anitrend.domain.user.entities

import co.anitrend.domain.common.entity.IEntity
import co.anitrend.domain.common.entity.IEntityImage

/** [User](https://anilist.github.io/ApiV2-GraphQL-Docs/user.doc.html)
 * A user from the anilist platform without the favourites, media options and stats
 *
 * @property id The id of the user
 * @property name The name of the user
 * @property about The bio written by user
 * @property avatar The user's avatar images
 * @property bannerImage The user's banner images
 * @property isFollowing If the authenticated user if following this user
 * @property options The user's general options
 * @property unreadNotificationCount The number of unread notifications the user has
 * @property siteUrl The url for the user page on the AniList website
 * @property donatorTier The donation tier of the user
 * @property moderatorStatus If the user is a moderator or data moderator
 * @property updatedAt When the user's data was last updated
 */
interface IUser : IEntity {
    val name: String
    val about: String?
    val avatar: IEntityImage?
    val bannerImage: String?
    val isFollowing: Boolean?
    val options: IUserOptions?
    val unreadNotificationCount: Int?
    val siteUrl: String?
    val donatorTier: Int?
    val moderatorStatus: String?
    val updatedAt: Long?
}