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
