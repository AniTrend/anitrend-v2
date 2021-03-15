/*
 * Copyright (C) 2020  AniTrend
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

package co.anitrend.data.user.model.contract

import co.anitrend.data.shared.common.Identity
import co.anitrend.data.shared.model.SharedImage

/** [User](https://anilist.github.io/ApiV2-GraphQL-Docs/user.doc.html)
 * A user from the anilist platform, media options and stats
 *
 * @property id The id of the user
 * @property name The name of the user
 * @property avatar The user's avatar images
 * @property bannerImage The user's banner images
 * @property isFollowing If the authenticated user if following this user
 * @property isFollower If this user if following the authenticated user
 * @property isBlocked If the user is blocked by the authenticated user
 * @property siteUrl The url for the user page on the AniList website
 * @property donatorTier The donation tier of the user
 * @property donatorBadge Custom donation badge text
 * @property updatedAt When the user's data was last updated
 * @property siteUrl The url for the user page on the AniList website
 * @property updatedAt When the user's data was last updated
 */
internal interface IUserModel : Identity {
    val name: String
    val avatar: SharedImage?
    val bannerImage: String?
    val isFollowing: Boolean?
    val isFollower: Boolean?
    val isBlocked: Boolean?
    val about: String?
    val donatorTier: Int?
    val donatorBadge: String?
    val siteUrl: String
    val updatedAt: Long?
}