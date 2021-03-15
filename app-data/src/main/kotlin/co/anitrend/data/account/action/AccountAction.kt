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

package co.anitrend.data.account.action

import co.anitrend.data.arch.common.model.graph.IGraphPayload
import co.anitrend.domain.account.model.AccountParam

internal sealed class AccountAction : IGraphPayload {

    override fun toMap(): Map<String, Any?> = emptyMap()

    data class SignIn(
        val param: AccountParam.SignIn,
    ) : AccountAction() {

        val expiresAtTime = (System.currentTimeMillis() / 1000) + param.expiresIn

        val accessTokenBearer = "Bearer ${param.accessToken}"

        /**
         * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
         */
        override fun toMap() = mapOf(
            "accessToken" to param.accessToken,
            "tokenType" to param.tokenType,
            "expiresIn" to param.expiresIn
        )
    }

    data class SignOut(
        val param: AccountParam.SignOut
    ) : AccountAction()
}