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

package co.anitrend.data.model.response.core.user

import androidx.room.Entity
import androidx.room.PrimaryKey
import co.anitrend.data.model.response.core.user.contract.IUser
import co.anitrend.data.model.response.meta.CoverImage
import co.anitrend.data.model.response.meta.Favourites
import co.anitrend.data.model.response.core.medialist.MediaListOptions

/** [User](https://anilist.github.io/ApiV2-GraphQL-Docs/user.doc.html)
 * A user from the anilist platform
 *
 * @param mediaListOptions The user's media list options
 * @param favourites The users favourites
 */
@Entity
data class User(
    val mediaListOptions: MediaListOptions?,
    val favourites: Favourites?,
    val statistics: UserStatisticTypes?,
    @PrimaryKey
    override val id: Long,
    override val name: String,
    override val about: String?,
    override val avatar: CoverImage?,
    override val bannerImage: String?,
    override val isFollowing: Boolean?,
    override val options: UserOptions?,
    override val unreadNotificationCount: Int?,
    override val siteUrl: String?,
    override val donatorTier: Int?,
    override val moderatorStatus: String?,
    override val updatedAt: Long?
) : IUser
