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

package co.anitrend.navigation.drawer.component.content.contract

import androidx.annotation.IdRes
import co.anitrend.core.android.components.sheet.action.contract.OnSlideAction
import co.anitrend.core.android.components.sheet.action.contract.OnStateChangedAction
import co.anitrend.navigation.drawer.model.navigation.Navigation
import kotlinx.coroutines.flow.Flow

interface INavigationDrawer {

    val navigationFlow: Flow<Navigation.Menu>

    fun isShowing(): Boolean
    fun toggleDrawer()
    fun show()
    fun dismiss()

    fun addOnSlideAction(action: OnSlideAction)
    fun removeOnSlideAction(action: OnSlideAction)
    fun addOnStateChangedAction(action: OnStateChangedAction)
    fun removeOnStateChangedAction(action: OnStateChangedAction)

    suspend fun setCheckedItem(@IdRes selectedItem: Int)
    fun toggleMenuVisibility(showDrawerMenu: Boolean)
}
