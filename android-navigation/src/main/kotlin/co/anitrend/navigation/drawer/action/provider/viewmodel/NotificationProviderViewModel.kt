/*
 * Copyright (C) 2022  AniTrend
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

package co.anitrend.navigation.drawer.action.provider.viewmodel

import androidx.lifecycle.map
import co.anitrend.core.component.viewmodel.AniTrendViewModel
import co.anitrend.domain.user.entity.User
import co.anitrend.navigation.drawer.action.provider.viewmodel.state.AuthenticatedUserState
import timber.log.Timber

internal class NotificationProviderViewModel(
    val state: AuthenticatedUserState
) : AniTrendViewModel(state) {

    val unreadNotifications = state.model.map { user ->
        when (user) {
            is User.Authenticated -> user.unreadNotifications
            else -> {
                Timber.w("Type of $user does not has no `unreadNotifications` property")
                0
            }
        }
    }

    init {
        fetchUser()
    }

    fun fetchUser() {
        runCatching(state::invoke)
            .onFailure(Timber::w)
    }
}