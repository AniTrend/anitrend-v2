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
package co.anitrend.android.navigation.compose.drawer.component.screen

import androidx.compose.runtime.Composable
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.android.navigation.drawer.R
import co.anitrend.android.navigation.drawer.model.account.Account
import co.anitrend.android.navigation.drawer.model.internal.DrawerDestination
import co.anitrend.android.navigation.drawer.model.internal.DrawerEntry
import co.anitrend.android.navigation.drawer.model.internal.DrawerUiState
import co.anitrend.domain.common.entity.contract.ICoverImage

private object NavigationDrawerSheetPreviewDefaults {
    private val activeAccount =
        Account.Authenticated(
            id = 7L,
            isActiveUser = true,
            userName = "Lena",
            coverImage = previewCoverImage("https://example.com/avatar/lena-large.png"),
        )

    private val alternateAccount =
        Account.Authenticated(
            id = 11L,
            isActiveUser = false,
            userName = "Kai",
            coverImage = previewCoverImage("https://example.com/avatar/kai-large.png"),
        )

    private val navigationEntries =
        listOf(
            DrawerEntry.Header(
                groupId = R.id.navigation_group_general,
                titleRes = R.string.navigation_header_general,
            ),
            DrawerEntry.Item(
                destination = DrawerDestination.Home,
                iconRes = R.drawable.ic_deck_24dp,
                titleRes = R.string.navigation_home,
                isCheckable = true,
                isChecked = true,
            ),
            DrawerEntry.Item(
                destination = DrawerDestination.Discover,
                iconRes = R.drawable.ic_discover_24dp,
                titleRes = R.string.navigation_discover,
                isCheckable = true,
            ),
            DrawerEntry.Item(
                destination = DrawerDestination.Social,
                iconRes = R.drawable.ic_social_24,
                titleRes = R.string.navigation_social,
                isCheckable = true,
            ),
            DrawerEntry.Item(
                destination = DrawerDestination.Reviews,
                iconRes = R.drawable.ic_review_24,
                titleRes = R.string.navigation_review,
                isCheckable = true,
            ),
            DrawerEntry.Item(
                destination = DrawerDestination.Suggestions,
                iconRes = R.drawable.ic_suggestions_24,
                titleRes = R.string.navigation_suggestions,
                isCheckable = true,
            ),
            DrawerEntry.Header(
                groupId = R.id.navigation_group_manage,
                titleRes = R.string.navigation_header_manage,
            ),
            DrawerEntry.Item(
                destination = DrawerDestination.AnimeList,
                iconRes = R.drawable.ic_anime_24,
                titleRes = R.string.navigation_anime_list,
                isCheckable = true,
            ),
            DrawerEntry.Item(
                destination = DrawerDestination.MangaList,
                iconRes = R.drawable.ic_manga_24,
                titleRes = R.string.navigation_manga_list,
                isCheckable = true,
            ),
            DrawerEntry.Header(
                groupId = R.id.navigation_group_catalog,
                titleRes = R.string.navigation_header_catalogs,
            ),
            DrawerEntry.Item(
                destination = DrawerDestination.News,
                iconRes = R.drawable.ic_news_24,
                titleRes = R.string.navigation_news,
                isCheckable = true,
            ),
            DrawerEntry.Item(
                destination = DrawerDestination.Forums,
                iconRes = R.drawable.ic_forum_24,
                titleRes = R.string.navigation_forums,
                isCheckable = true,
            ),
            DrawerEntry.Item(
                destination = DrawerDestination.Episodes,
                iconRes = R.drawable.ic_tv_24dp,
                titleRes = R.string.navigation_episodes,
                isCheckable = true,
            ),
            DrawerEntry.Header(
                groupId = R.id.navigation_group_support,
                titleRes = R.string.navigation_header_support,
            ),
            DrawerEntry.Item(
                destination = DrawerDestination.ExternalUrl(url = "https://anitrend.co/support"),
                iconRes = R.drawable.ic_patreon_24dp,
                titleRes = R.string.navigation_support,
                isCheckable = false,
            ),
            DrawerEntry.Item(
                destination = DrawerDestination.ExternalUrl(url = "https://discord.gg/anitrend"),
                iconRes = R.drawable.ic_discord_24dp,
                titleRes = R.string.navigation_discord,
                isCheckable = false,
            ),
            DrawerEntry.Item(
                destination = DrawerDestination.ExternalUrl(url = "https://docs.anitrend.co/project/faq"),
                iconRes = R.drawable.ic_help_24dp,
                titleRes = R.string.navigation_faq,
                isCheckable = false,
            ),
        )

    private val accountSwitcherEntries =
        listOf(
            Account.Group(
                titleRes = R.string.account_header_active,
                groupId = R.id.account_group_active,
            ),
            activeAccount,
            Account.Group(
                titleRes = R.string.account_header_inactive,
                groupId = R.id.account_group_inactive,
            ),
            alternateAccount,
            Account.Group(
                titleRes = R.string.account_header_other,
                groupId = R.id.account_group_other,
            ),
            Account.Anonymous(
                titleRes = R.string.label_account_anonymous,
                imageRes = co.anitrend.core.R.mipmap.ic_launcher,
                isActiveUser = false,
            ),
            Account.Authorize(
                titleRes = R.string.label_account_add_new,
            ),
        )

    val populatedNavigation =
        DrawerUiState(
            entries = navigationEntries,
            selectedDestination = DrawerDestination.Home,
            accounts = accountSwitcherEntries,
            activeAccount = activeAccount,
            isSheetVisible = true,
            isAccountSwitcherExpanded = false,
        )

    val populatedAccountSwitcher =
        populatedNavigation.copy(
            isAccountSwitcherExpanded = true,
        )

    val anonymousFallback =
        DrawerUiState(
            entries = navigationEntries,
            selectedDestination = DrawerDestination.Discover,
            accounts =
                listOf(
                    Account.Group(
                        titleRes = R.string.account_header_active,
                        groupId = R.id.account_group_active,
                    ),
                    Account.Anonymous(
                        titleRes = R.string.label_account_anonymous,
                        imageRes = co.anitrend.core.R.mipmap.ic_launcher,
                        isActiveUser = true,
                    ),
                    Account.Group(
                        titleRes = R.string.account_header_other,
                        groupId = R.id.account_group_other,
                    ),
                    Account.Authorize(
                        titleRes = R.string.label_account_add_new,
                    ),
                ),
            activeAccount =
                Account.Anonymous(
                    titleRes = R.string.label_account_anonymous,
                    imageRes = co.anitrend.core.R.mipmap.ic_launcher,
                    isActiveUser = true,
                ),
            isSheetVisible = true,
            isAccountSwitcherExpanded = false,
        )

    // No explicit loading state exists yet, so this previews the most lightweight degraded shell.
    val lightweightDegraded =
        DrawerUiState(
            entries = emptyList(),
            selectedDestination = DrawerDestination.Home,
            accounts = emptyList(),
            activeAccount = null,
            isSheetVisible = true,
            isAccountSwitcherExpanded = false,
        )

    private fun previewCoverImage(url: String): ICoverImage =
        object : ICoverImage {
            override val large: CharSequence = url
            override val medium: CharSequence = url
        }
}

@Composable
private fun NavigationDrawerSheetScreenPreview(uiState: DrawerUiState) {
    PreviewTheme(wrapInSurface = true) {
        NavigationDrawerSheetScreen(
            uiState = uiState,
            onHeaderClick = {},
            onAccountClick = {},
            onNavigationClick = {},
        )
    }
}

@AniTrendPreview.Light
@AniTrendPreview.Dark
@Composable
private fun NavigationDrawerSheetScreenPopulatedPreview() {
    NavigationDrawerSheetScreenPreview(
        uiState = NavigationDrawerSheetPreviewDefaults.populatedNavigation,
    )
}

@AniTrendPreview.Mobile
@Composable
private fun NavigationDrawerSheetScreenAccountSwitcherPreview() {
    NavigationDrawerSheetScreenPreview(
        uiState = NavigationDrawerSheetPreviewDefaults.populatedAccountSwitcher,
    )
}

@AniTrendPreview.Light
@AniTrendPreview.Dark
@Composable
private fun NavigationDrawerSheetScreenAnonymousPreview() {
    NavigationDrawerSheetScreenPreview(
        uiState = NavigationDrawerSheetPreviewDefaults.anonymousFallback,
    )
}

@AniTrendPreview.Mobile
@Composable
private fun NavigationDrawerSheetScreenDegradedPreview() {
    NavigationDrawerSheetScreenPreview(
        uiState = NavigationDrawerSheetPreviewDefaults.lightweightDegraded,
    )
}
