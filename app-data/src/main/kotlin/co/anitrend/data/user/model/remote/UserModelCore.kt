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

package co.anitrend.data.user.model.remote

import co.anitrend.data.shared.model.SharedImage
import co.anitrend.data.user.model.contract.IUserModelCore

/** [User](https://anilist.github.io/ApiV2-GraphQL-Docs/user.doc.html)
 * A user from the anilist platform
 *
 * @param mediaListOptions The user's media list options
 * @param favourites The users favourites
 */
internal data class UserModelCore(
    override val id: Long,
    override val name: String,
    override val avatar: SharedImage?,
    override val bannerImage: String?,
    override val isFollowing: Boolean?,
    override val isFollower: Boolean?,
) : IUserModelCore
