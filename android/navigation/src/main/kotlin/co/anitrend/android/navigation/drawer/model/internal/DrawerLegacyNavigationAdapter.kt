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

import androidx.annotation.IdRes
import co.anitrend.android.navigation.drawer.R
import co.anitrend.android.navigation.drawer.model.navigation.Navigation

internal object DrawerLegacyNavigationAdapter {
    fun map(entries: List<DrawerEntry>): List<Navigation> =
        entries.mapNotNull { entry ->
            when (entry) {
                is DrawerEntry.Header ->
                    Navigation.Group(
                        titleRes = entry.titleRes,
                        groupId = entry.groupId,
                    )

                is DrawerEntry.Item -> toLegacy(entry)
            }
        }

    fun toLegacy(item: DrawerEntry.Item): Navigation.Menu? {
        val itemId = legacyMenuIdFor(item.destination) ?: return null
        return Navigation.Menu(
            id = itemId,
            icon = item.iconRes,
            titleRes = item.titleRes,
            isCheckable = item.isCheckable,
            isChecked = item.isChecked,
        )
    }

    fun fromLegacyMenuId(
        @IdRes itemId: Int,
    ): DrawerDestination? =
        when (itemId) {
            R.id.navigation_home -> DrawerDestination.Home
            R.id.navigation_discover -> DrawerDestination.Discover
            R.id.navigation_social -> DrawerDestination.Social
            R.id.navigation_reviews -> DrawerDestination.Reviews
            R.id.navigation_suggestions -> DrawerDestination.Suggestions
            R.id.navigation_anime_list -> DrawerDestination.AnimeList
            R.id.navigation_manga_list -> DrawerDestination.MangaList
            R.id.navigation_news -> DrawerDestination.News
            R.id.navigation_forum -> DrawerDestination.Forums
            R.id.navigation_episodes -> DrawerDestination.Episodes
            R.id.navigation_donate -> DrawerDestination.ExternalUrl(PATREON_URL)
            R.id.navigation_discord -> DrawerDestination.ExternalUrl(DISCORD_URL)
            R.id.navigation_faq -> DrawerDestination.ExternalUrl(FAQ_URL)
            else -> null
        }

    @IdRes
    fun legacyMenuIdFor(destination: DrawerDestination): Int? =
        when (destination) {
            DrawerDestination.Home -> R.id.navigation_home
            DrawerDestination.Discover -> R.id.navigation_discover
            DrawerDestination.Social -> R.id.navigation_social
            DrawerDestination.Reviews -> R.id.navigation_reviews
            DrawerDestination.Suggestions -> R.id.navigation_suggestions
            DrawerDestination.AnimeList -> R.id.navigation_anime_list
            DrawerDestination.MangaList -> R.id.navigation_manga_list
            DrawerDestination.News -> R.id.navigation_news
            DrawerDestination.Forums -> R.id.navigation_forum
            DrawerDestination.Episodes -> R.id.navigation_episodes
            is DrawerDestination.ExternalUrl ->
                when (destination.url) {
                    PATREON_URL -> R.id.navigation_donate
                    DISCORD_URL -> R.id.navigation_discord
                    FAQ_URL -> R.id.navigation_faq
                    else -> null
                }
        }

    private const val PATREON_URL = "https://www.patreon.com/wax911"
    private const val DISCORD_URL = "https://discord.gg/2wzTqnF"
    private const val FAQ_URL = "https://docs.anitrend.co/project/faq"
}
