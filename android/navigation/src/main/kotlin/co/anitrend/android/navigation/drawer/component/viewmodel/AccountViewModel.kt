/*
 * Copyright (C) 2020 AniTrend
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
package co.anitrend.android.navigation.drawer.component.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import co.anitrend.core.component.viewmodel.state.AniTrendViewModelState
import co.anitrend.data.account.AccountInteractor
import co.anitrend.domain.user.entity.User
import co.anitrend.android.navigation.drawer.component.viewmodel.mapper.UsersToAccountsMapper
import co.anitrend.android.navigation.drawer.model.account.Account
import kotlinx.coroutines.launch

internal class AccountViewModel(
    private val mapper: UsersToAccountsMapper,
    private val interactor: AccountInteractor,
) : AniTrendViewModelState<List<User>>() {
    val userAccounts: LiveData<List<Account>> = model.map(mapper::invoke)

    val activeAccount: LiveData<Account?> =
        userAccounts.map {
            it.singleOrNull(Account::isActiveUser)
        }

    operator fun invoke() {
        viewModelScope.launch {
            val result = interactor.getAuthorizedAccounts()
            state.postValue(result)
        }
    }
}
