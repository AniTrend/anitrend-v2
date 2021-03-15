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

package co.anitrend.data.user.model.container

import co.anitrend.data.arch.common.model.paging.info.PageInfo
import co.anitrend.data.user.model.UserModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal sealed class UserModelContainer {

    @Serializable
    data class Id(
        @SerialName("User") val user: UserModel.Id
    ) : UserModelContainer()

    @Serializable
    data class User(
        @SerialName("User") val user: UserModel.Core
    ) : UserModelContainer()

    @Serializable
    data class Profile(
        @SerialName("User") val user: UserModel.Extended
    ) : UserModelContainer()

    @Serializable
    data class Viewer(
        @SerialName("Viewer") val user: UserModel.Extended
    ) : UserModelContainer()

    @Serializable
    data class WithStatistic(
        @SerialName("User") val user: UserModel.WithStatistic
    ) : UserModelContainer()

    @Serializable
    data class Paged(
        @SerialName("Page") val page: Page = Page()
    ) : UserModelContainer() {
        @Serializable
        data class Page(
            @SerialName("pageInfo") val pageInfo: PageInfo? = null,
            @SerialName("users") val userList: List<UserModel.Core> = emptyList()
        )
    }
}
