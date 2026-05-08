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
package co.anitrend.android.navigation.drawer.model.internal

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import co.anitrend.android.navigation.drawer.model.account.Account

@Immutable
sealed interface DrawerEntry {
    val stableId: String

    @Immutable
    data class Header(
        val groupId: Int,
        @StringRes val titleRes: Int,
        override val stableId: String = "header:$groupId",
    ) : DrawerEntry

    @Immutable
    data class Item(
        val destination: DrawerDestination,
        @DrawableRes val iconRes: Int,
        @StringRes val titleRes: Int,
        val isCheckable: Boolean,
        val isChecked: Boolean = false,
        override val stableId: String = "item:$destination",
    ) : DrawerEntry
}

@Immutable
sealed interface DrawerDestination {
    data object Home : DrawerDestination

    data object Discover : DrawerDestination

    data object Social : DrawerDestination

    data object Reviews : DrawerDestination

    data object Suggestions : DrawerDestination

    data object AnimeList : DrawerDestination

    data object MangaList : DrawerDestination

    data object News : DrawerDestination

    data object Forums : DrawerDestination

    data object Episodes : DrawerDestination

    @Immutable
    data class ExternalUrl(
        val url: String,
    ) : DrawerDestination
}

@Immutable
sealed interface DrawerEvent {
    @Immutable
    data class Navigate(
        val item: DrawerEntry.Item,
    ) : DrawerEvent
}

@Immutable
data class DrawerUiState(
    val entries: List<DrawerEntry> = emptyList(),
    val selectedDestination: DrawerDestination = DrawerDestination.Home,
    val accounts: List<Account> = emptyList(),
    val activeAccount: Account? = null,
    val isSheetVisible: Boolean = false,
    val isAccountSwitcherExpanded: Boolean = false,
)
