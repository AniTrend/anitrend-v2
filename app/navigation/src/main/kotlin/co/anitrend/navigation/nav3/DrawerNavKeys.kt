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
package co.anitrend.navigation.nav3

import kotlinx.serialization.Serializable

/**
 * Nav3 keys for the main navigation drawer destinations.
 *
 * These correspond to the legacy Navigation.Menu IDs and provide
 * serializable NavKey equivalents for each drawer item.
 *
 * Payloads stay compact — media types, sort orders, and IDs only.
 */

@Serializable
data object HomeNavKey : AniTrendNavKey

@Serializable
data object DiscoverNavKey : AniTrendNavKey

@Serializable
data object NewsNavKey : AniTrendNavKey

@Serializable
data object EpisodesNavKey : AniTrendNavKey

@Serializable
data object ReviewsNavKey : AniTrendNavKey

@Serializable
data object SuggestionsNavKey : AniTrendNavKey

@Serializable
data object SocialNavKey : AniTrendNavKey

@Serializable
data class AnimeListNavKey(
    val userId: Long,
) : AniTrendNavKey

@Serializable
data class MangaListNavKey(
    val userId: Long,
) : AniTrendNavKey

@Serializable
data object ForumsNavKey : AniTrendNavKey
