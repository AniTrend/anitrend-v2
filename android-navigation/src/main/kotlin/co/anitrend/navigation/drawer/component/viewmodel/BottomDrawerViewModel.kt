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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.anitrend.core.settings.Settings
import co.anitrend.navigation.drawer.component.viewmodel.state.AccountState
import co.anitrend.navigation.drawer.component.viewmodel.state.NavigationState
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

internal class BottomDrawerViewModel(
    val navigationState: NavigationState,
    val accountState: AccountState,
    authenticatedKey: String,
    settings: Settings
) : ViewModel() {

    init {
        navigationState(settings.isAuthenticated)
        viewModelScope.launch {
            observeAuthenticationPreference(settings, authenticatedKey)
        }
    }

    private suspend fun observeAuthenticationPreference(
        settings: Settings,
        authenticatedKey: String
    ) {
        settings.preferenceChangeFlow
            .filter {
                it.key == authenticatedKey
            }.onEach {
                accountState()
                navigationState(
                    settings.isAuthenticated
                )
            }.catch { cause: Throwable ->
                Timber.tag(moduleTag).e(
                    cause,
                    "settings.preferenceChangeFlow -> Threw an uncaught exception"
                )
            }.collect()
    }

    /**
     * This method will be called when this ViewModel is no longer used and will be destroyed.
     *
     *
     * It is useful when ViewModel observes some data and you need to clear this subscription to
     * prevent a leak of this ViewModel.
     */
    override fun onCleared() {
        super.onCleared()
        navigationState.onCleared()
        accountState.onCleared()
    }

    companion object {
        private val moduleTag = BottomDrawerViewModel::class.java.simpleName
    }
}