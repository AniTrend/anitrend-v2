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

package co.anitrend.navigation.drawer.component.viewmodel.state

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import co.anitrend.arch.core.model.ISupportViewModelState
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.core.settings.Settings
import co.anitrend.navigation.drawer.R
import co.anitrend.navigation.drawer.model.account.Account
import kotlinx.coroutines.CoroutineScope

internal class AccountState(
    private val scope: CoroutineScope,
    private val settings: Settings
) : ISupportViewModelState<List<Account>> {

    private var initialState = mutableListOf(
        Account.Anonymous(
            titleRes = R.string.label_account_anonymous,
            imageRes = R.mipmap.ic_launcher_round,
            isActiveUser = true
        ),
        Account.Authorize(
            titleRes = R.string.label_account_add_new
        )
    )

    private val _accountItems = MutableLiveData<List<Account>>(initialState)

    override val model: LiveData<List<Account>?>
        get() = _accountItems

    override val networkState: LiveData<NetworkState> = liveData {
        emit(NetworkState.Loading)
    }

    override val refreshState: LiveData<NetworkState>? = liveData {
        emit(NetworkState.Loading)
    }

    /**
     * Called upon [androidx.lifecycle.ViewModel.onCleared] and should optionally
     * call cancellation of any ongoing jobs.
     *
     * If your use case source is of type [co.anitrend.arch.domain.common.IUseCase]
     * then you could optionally call [co.anitrend.arch.domain.common.IUseCase.onCleared] here
     */
    override fun onCleared() {

    }

    /**
     * Triggers use case to perform refresh operation
     */
    override suspend fun refresh() {
        TODO("Not yet implemented")
    }

    /**
     * Triggers use case to perform a retry operation
     */
    override suspend fun retry() {
        TODO("Not yet implemented")
    }
}