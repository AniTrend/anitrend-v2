/*
 * Copyright (C) 2026 AniTrend
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
package co.anitrend.settings.component.content.anilist.viewmodel

import androidx.lifecycle.viewModelScope
import co.anitrend.core.component.viewmodel.state.AniTrendViewModelState
import co.anitrend.data.user.GetAuthenticatedInteractor
import co.anitrend.domain.user.entity.User
import kotlinx.coroutines.launch

class AniListSettingsViewModel(
    private val interactor: GetAuthenticatedInteractor,
) : AniTrendViewModelState<User>() {
    private var hasLoaded = false

    fun load() {
        if (hasLoaded) {
            return
        }

        hasLoaded = true
        viewModelScope.launch {
            state.postValue(interactor())
        }
    }

    fun sync() {
        viewModelScope.launch {
            if (state.value == null) {
                state.postValue(interactor())
            } else {
                refresh()
            }
        }
    }
}
