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

import co.anitrend.android.navigation.drawer.R

internal object DrawerResourceRegistry {
    val titleResByKey =
        mapOf(
            "navigation_home" to R.string.navigation_home,
            "navigation_discover" to R.string.navigation_discover,
            "navigation_social" to R.string.navigation_social,
            "navigation_review" to R.string.navigation_review,
            "navigation_suggestions" to R.string.navigation_suggestions,
            "navigation_anime_list" to R.string.navigation_anime_list,
            "navigation_manga_list" to R.string.navigation_manga_list,
            "navigation_news" to R.string.navigation_news,
            "navigation_forums" to R.string.navigation_forums,
            "navigation_episodes" to R.string.navigation_episodes,
            "navigation_support" to R.string.navigation_support,
            "navigation_discord" to R.string.navigation_discord,
            "navigation_faq" to R.string.navigation_faq,
            "navigation_header_general" to R.string.navigation_header_general,
            "navigation_header_manage" to R.string.navigation_header_manage,
            "navigation_header_catalogs" to R.string.navigation_header_catalogs,
            "navigation_header_support" to R.string.navigation_header_support,
        )

    val iconResByKey =
        mapOf(
            "ic_deck_24dp" to R.drawable.ic_deck_24dp,
            "ic_discover_24dp" to R.drawable.ic_discover_24dp,
            "ic_social_24" to R.drawable.ic_social_24,
            "ic_review_24" to R.drawable.ic_review_24,
            "ic_suggestions_24" to R.drawable.ic_suggestions_24,
            "ic_anime_24" to R.drawable.ic_anime_24,
            "ic_manga_24" to R.drawable.ic_manga_24,
            "ic_news_24" to R.drawable.ic_news_24,
            "ic_forum_24" to R.drawable.ic_forum_24,
            "ic_tv_24dp" to R.drawable.ic_tv_24dp,
            "ic_patreon_24dp" to R.drawable.ic_patreon_24dp,
            "ic_discord_24dp" to R.drawable.ic_discord_24dp,
            "ic_help_24dp" to R.drawable.ic_help_24dp,
        )

    val groupIdByKey =
        mapOf(
            "navigation_header_general" to R.id.navigation_group_general,
            "navigation_header_manage" to R.id.navigation_group_manage,
            "navigation_header_catalogs" to R.id.navigation_group_catalog,
            "navigation_header_support" to R.id.navigation_group_support,
        )
}
