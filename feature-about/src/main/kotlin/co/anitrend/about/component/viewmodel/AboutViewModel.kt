/*
 * Copyright (C) 2025 AniTrend
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
package co.anitrend.about.component.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.navigation.model.common.IParam

internal class AboutViewModel : ViewModel() {
    val loadState: MutableLiveData<LoadState> = MutableLiveData(LoadState.Loading())

    operator fun invoke(param: IParam) {
        when (param) {
            is IParam.None -> loadState.postValue(LoadState.Idle())
            else -> Unit
        }
    }
}
