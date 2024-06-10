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

package co.anitrend.data.account.repository

import co.anitrend.arch.data.state.DataState
import co.anitrend.arch.data.state.DataState.Companion.create
import co.anitrend.data.auth.source.contract.AuthSource
import co.anitrend.domain.account.model.AccountParam
import co.anitrend.domain.account.repository.AccountRepository
import co.anitrend.domain.user.entity.User

internal class AccountRepositoryImpl(
    private val source: AuthSource
) : AccountRepository<DataState<List<User>>>{

    override suspend fun getAccountUsers() =
        source create source()

    override suspend fun signOut(param: AccountParam.SignOut) {
        source.signOut(param)
    }

    override suspend fun signIn(param: AccountParam.Activate) {
        source.signIn(param)
    }
}
