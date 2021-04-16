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

package co.anitrend.data.user.model.query

import co.anitrend.data.common.model.graph.IGraphPayload
import co.anitrend.data.auth.settings.IAuthenticationSettings
import co.anitrend.domain.user.model.UserParam

internal sealed class UserQuery : IGraphPayload {

    data class Identifier(
        val param: UserParam.Identifier
    ) : UserQuery() {

        /**
         * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
         */
        override fun toMap() = mapOf(
            "name" to param.name,
            "id" to param.id
        )
    }

    data class Profile(
        val param: UserParam.Profile
    ) : UserQuery() {

        fun isUserIdValid() =
            param.id != IAuthenticationSettings.INVALID_USER_ID

        /**
         * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
         */
        override fun toMap() = mapOf(
            "id" to param.id
        )
    }

    data class Statistic(
        val param: UserParam.Statistic
    ) : UserQuery() {

        fun isUserIdValid() =
            param.id != IAuthenticationSettings.INVALID_USER_ID

        /**
         * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
         */
        override fun toMap() = mapOf(
            "id" to param.id,
            "statisticsSort" to param.statisticsSort
        )
    }
    
    data class Search(
        val param: UserParam.Search
    ) : UserQuery() {

        /**
         * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
         */
        override fun toMap() = mapOf(
            "search" to param.search,
            "sort" to param.sort
        )
    }


}