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

package co.anitrend.domain.user.entity.contract

/**
 * A users status
 *
 * @param about The users bio
 * @param donationBadge Custom donation badge text
 * @param donationTier The donation tier of the user
 * @param isFollowing If the authenticated user if following this user
 * @param isFollower If this user if following the authenticated user
 * @param isBlocked If the user is blocked by the authenticated user
 * @param pageUrl The url for the user page on the AniList website
 */
data class UserStatus(
    val about: CharSequence?,
    val donationBadge: CharSequence?,
    val donationTier: Int?,
    val isFollowing: Boolean?,
    val isFollower: Boolean?,
    val isBlocked: Boolean?,
    val pageUrl: CharSequence?,
    val createdAt: Long?,
    val updatedAt: Long?,
) {
    companion object {
        fun empty() = UserStatus(
            about = null,
            donationBadge = null,
            donationTier = null,
            isFollowing = null,
            isFollower = null,
            isBlocked = null,
            pageUrl = null,
            createdAt = null,
            updatedAt = null,
        )
    }
}