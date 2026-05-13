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

import co.anitrend.domain.config.entity.Config

internal object DrawerDefaults {
    val navigation =
        listOf(
            navigationEntry(
                destination = "/home",
                groupKey = "navigation_header_general",
                iconKey = "ic_deck_24dp",
                titleKey = "navigation_home",
            ),
            navigationEntry(
                destination = "/discover",
                groupKey = "navigation_header_general",
                iconKey = "ic_discover_24dp",
                titleKey = "navigation_discover",
            ),
            navigationEntry(
                destination = "/social",
                groupKey = "navigation_header_general",
                iconKey = "ic_social_24",
                titleKey = "navigation_social",
            ),
            navigationEntry(
                destination = "/reviews",
                groupKey = "navigation_header_general",
                iconKey = "ic_review_24",
                titleKey = "navigation_review",
            ),
            navigationEntry(
                destination = "/suggestions",
                groupKey = "navigation_header_general",
                iconKey = "ic_suggestions_24",
                titleKey = "navigation_suggestions",
            ),
            navigationEntry(
                destination = "/animelist",
                groupKey = "navigation_header_manage",
                iconKey = "ic_anime_24",
                titleKey = "navigation_anime_list",
                authenticated = true,
            ),
            navigationEntry(
                destination = "/mangalist",
                groupKey = "navigation_header_manage",
                iconKey = "ic_manga_24",
                titleKey = "navigation_manga_list",
                authenticated = true,
            ),
            navigationEntry(
                destination = "/news",
                groupKey = "navigation_header_catalogs",
                iconKey = "ic_news_24",
                titleKey = "navigation_news",
            ),
            navigationEntry(
                destination = "/forum/recent",
                groupKey = "navigation_header_catalogs",
                iconKey = "ic_forum_24",
                titleKey = "navigation_forums",
            ),
            navigationEntry(
                destination = "/episodes",
                groupKey = "navigation_header_catalogs",
                iconKey = "ic_tv_24dp",
                titleKey = "navigation_episodes",
            ),
            navigationEntry(
                destination = "https://www.patreon.com/wax911",
                groupKey = "navigation_header_support",
                iconKey = "ic_patreon_24dp",
                titleKey = "navigation_support",
            ),
            navigationEntry(
                destination = "https://discord.gg/2wzTqnF",
                groupKey = "navigation_header_support",
                iconKey = "ic_discord_24dp",
                titleKey = "navigation_discord",
            ),
            navigationEntry(
                destination = "https://docs.anitrend.co/project/faq",
                groupKey = "navigation_header_support",
                iconKey = "ic_help_24dp",
                titleKey = "navigation_faq",
            ),
        )

    private fun navigationEntry(
        destination: String,
        groupKey: String,
        iconKey: String,
        titleKey: String,
        authenticated: Boolean = false,
        id: Long = destination.hashCode().toLong(),
    ) = Config.Navigation(
        criteria = ">=2.0.0",
        destination = destination,
        group =
            Config.Navigation.Group(
                authenticated = authenticated,
                i18n = groupKey,
            ),
        i18n = titleKey,
        icon = iconKey,
        id = id,
    )
}
