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

package co.anitrend.navigation.drawer.component.viewmodel

import androidx.lifecycle.map
import co.anitrend.core.component.viewmodel.AniTrendViewModel
import co.anitrend.navigation.drawer.component.viewmodel.mapper.UsersToAccountsMapper
import co.anitrend.navigation.drawer.component.viewmodel.state.AccountState
import co.anitrend.navigation.drawer.model.account.Account

internal class AccountViewModel(
    mapper: UsersToAccountsMapper,
    val accountState: AccountState,
) : AniTrendViewModel(accountState) {

    val userAccounts = accountState.model.map {
        mapper(it)
    }

    val activeAccount = userAccounts.map {
        it.singleOrNull(Account::isActiveUser)
    }
}