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

package co.anitrend.data.auth.source.contract

import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.data.request.model.Request
import co.anitrend.arch.data.source.core.SupportCoreDataSource
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.arch.extension.ext.empty
import co.anitrend.data.account.action.AccountAction
import co.anitrend.domain.account.model.AccountParam
import co.anitrend.domain.user.entity.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

internal abstract class AuthSource : SupportCoreDataSource() {

    protected abstract val observable: Flow<User>
    protected abstract val observables: Flow<List<User>>

    abstract fun signOut(param: AccountParam.SignOut)

    protected abstract suspend fun getAuthorizedUser(
        param: AccountAction.SignIn,
        callback: RequestCallback
    )

    internal operator fun invoke(): Flow<List<User>> {
        return observables
    }

    internal operator fun invoke(param: AccountParam.SignIn): Flow<User> {
        launch {
            requestHelper.runIfNotRunning(
                Request.Default(String.empty(), Request.Type.INITIAL)
            ) { getAuthorizedUser(AccountAction.SignIn(param), it) }
        }
        return observable
    }
}