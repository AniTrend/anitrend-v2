/*
 * Copyright (C) 2023  AniTrend
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

import androidx.annotation.IdRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import co.anitrend.core.extensions.mutableStateFlow
import co.anitrend.data.auth.settings.IAuthenticationSettings
import co.anitrend.navigation.drawer.R
import co.anitrend.navigation.drawer.model.navigation.Navigation
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber

internal class NavigationViewModel(
    settings: IAuthenticationSettings,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val navigationItems: MutableStateFlow<List<Navigation>> by mutableStateFlow {
        createNavigationItems(settings.isAuthenticated.value)
    }

    private fun createNavigationItems(authenticated: Boolean): List<Navigation> {
        val navigationItems = mutableListOf<Navigation>()
        val generalItems = listOf(
            Navigation.Group(
                titleRes = R.string.navigation_header_general,
                groupId = R.id.navigation_group_general
            ),
            Navigation.Menu(
                id = R.id.navigation_home,
                icon = R.drawable.ic_deck_24dp,
                titleRes = R.string.navigation_home,
            ),
            Navigation.Menu(
                id = R.id.navigation_discover,
                icon = R.drawable.ic_discover_24dp,
                titleRes = R.string.navigation_discover,
            ),
            Navigation.Menu(
                id = R.id.navigation_social,
                icon = R.drawable.ic_social_24,
                titleRes = R.string.navigation_social,
            ),
            Navigation.Menu(
                id = R.id.navigation_reviews,
                icon = R.drawable.ic_review_24,
                titleRes = R.string.navigation_review,
            ),
            Navigation.Menu(
                id = R.id.navigation_suggestions,
                icon = R.drawable.ic_suggestions_24,
                titleRes = R.string.navigation_suggestions,
            )
        )
        val manageItems = listOf(
            Navigation.Group(
                titleRes = R.string.navigation_header_manage,
                groupId = R.id.navigation_group_manage
            ),
            Navigation.Menu(
                id = R.id.navigation_anime_list,
                icon = R.drawable.ic_anime_24,
                titleRes = R.string.navigation_anime_list,
            ),
            Navigation.Menu(
                id = R.id.navigation_manga_list,
                icon = R.drawable.ic_manga_24,
                titleRes = R.string.navigation_manga_list,
            )
        )
        val catalogItems = listOf(
            Navigation.Group(
                titleRes = R.string.navigation_header_catalogs,
                groupId = R.id.navigation_group_catalog
            ),
            Navigation.Menu(
                id = R.id.navigation_news,
                icon = R.drawable.ic_news_24,
                titleRes = R.string.navigation_news,
            ),
            Navigation.Menu(
                id = R.id.navigation_forum,
                icon = R.drawable.ic_forum_24,
                titleRes = R.string.navigation_forums,
            ),
            Navigation.Menu(
                id = R.id.navigation_episodes,
                icon = R.drawable.ic_tv_24dp,
                titleRes = R.string.navigation_episodes,
            )
        )
        val supportItems = listOf(
            Navigation.Group(
                titleRes = R.string.navigation_header_support,
                groupId = R.id.navigation_group_support
            ),
            Navigation.Menu(
                id = R.id.navigation_donate,
                icon = R.drawable.ic_patreon_24dp,
                titleRes = R.string.navigation_support,
                isCheckable = false
            ),
            Navigation.Menu(
                id = R.id.navigation_discord,
                icon = R.drawable.ic_discord_24dp,
                titleRes = R.string.navigation_discord,
                isCheckable = false
            ),
            Navigation.Menu(
                id = R.id.navigation_faq,
                icon = R.drawable.ic_help_24dp,
                titleRes = R.string.navigation_faq,
                isCheckable = false
            )
        )

        navigationItems.addAll(generalItems)
        if (authenticated)
            navigationItems.addAll(manageItems)
        navigationItems.addAll(catalogItems)
        navigationItems.addAll(supportItems)

        return navigationItems
    }

    suspend operator fun invoke(isAuthenticated: Boolean) {
        val checkedId: Int? = savedStateHandle[CHECKED_ITEM_ID]
        navigationItems.emit(createNavigationItems(isAuthenticated))
        if (checkedId != null) {
            // TODO: force navigate away from a selection that maybe have been removed
            Timber.i("Updating navigation selected item")
            setNavigationMenuItemChecked(checkedId)
        } else {
            Timber.w("SavedStateHandle did not have a valid state saved for: $CHECKED_ITEM_ID")
        }
    }

    /**
     * Set the currently selected menu item.
     *
     * @return true if the currently selected item has changed.
     */
    suspend fun setNavigationMenuItemChecked(@IdRes id: Int) {
        var updated = false
        val snapshot = mutableListOf<Navigation>()
        snapshot.addAll(navigationItems.value)
        snapshot.filterIsInstance<Navigation.Menu>()
            .onEach { menu ->
                val shouldCheck = menu.id == id
                if (menu.isChecked != shouldCheck) {
                    updated = true
                }
                menu.isChecked = shouldCheck
            }
        savedStateHandle[CHECKED_ITEM_ID] = id
        if (updated)
            navigationItems.emit(snapshot)
    }

    private companion object {
        const val CHECKED_ITEM_ID = "menu_checked_id"
    }
}
