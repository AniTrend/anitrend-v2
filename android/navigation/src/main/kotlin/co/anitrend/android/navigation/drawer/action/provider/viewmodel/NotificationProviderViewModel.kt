/*
 * Copyright (C) 2022 AniTrend
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
package co.anitrend.android.navigation.drawer.action.provider.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import co.anitrend.core.component.viewmodel.state.AniTrendViewModelState
import co.anitrend.data.user.GetAuthenticatedInteractor
import co.anitrend.domain.user.entity.User
import kotlinx.coroutines.launch
import timber.log.Timber

internal class NotificationProviderViewModel(
    private val interactor: GetAuthenticatedInteractor,
) : AniTrendViewModelState<User>() {
    val unreadNotifications: LiveData<Int> =
        model.map { user ->
            when (user) {
                is User.Authenticated -> user.unreadNotifications
                else -> {
                    Timber.w("Type of $user does not have `unreadNotifications` property")
                    0
                }
            }
        }

    fun fetchUser() {
        viewModelScope.launch {
            runCatching {
                val result = interactor()
                state.postValue(result)
            }.onFailure(Timber::w)
        }
    }
}
