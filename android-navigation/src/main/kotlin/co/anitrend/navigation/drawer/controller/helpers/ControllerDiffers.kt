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
package co.anitrend.navigation.drawer.controller.helpers

import androidx.recyclerview.widget.DiffUtil
import co.anitrend.navigation.drawer.model.account.Account
import co.anitrend.navigation.drawer.model.navigation.Navigation

internal object NavigationDiffUtil : DiffUtil.ItemCallback<Navigation>() {
    override fun areItemsTheSame(
        oldItem: Navigation,
        newItem: Navigation,
    ) = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: Navigation,
        newItem: Navigation,
    ) = oldItem.hashCode() == newItem.hashCode()
}

internal object AccountDiffUtil : DiffUtil.ItemCallback<Account>() {
    override fun areItemsTheSame(
        oldItem: Account,
        newItem: Account,
    ) = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: Account,
        newItem: Account,
    ) = oldItem.hashCode() == newItem.hashCode()
}
