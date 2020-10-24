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
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** [User](https://anilist.github.io/ApiV2-GraphQL-Docs/user.doc.html)
 * A user from the anilist platform
 *
 * @param mediaListOptions The user's media list options
 * @param favourites The users favourites
 */
@Serializable
internal data class UserModelCore(
    @SerialName("id") override val id: Long,
    @SerialName("name") override val name: String,
    @SerialName("avatar") override val avatar: SharedImage?,
    @SerialName("bannerImage") override val bannerImage: String?,
    @SerialName("isFollowing") override val isFollowing: Boolean?,
    @SerialName("isFollower") override val isFollower: Boolean?,
) : IUserModelCore
