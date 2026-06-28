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

/**
 * Koin singleton bridge for passing a resolved NavKey from [DeepLinkScreen]
 * to [MainComposeActivity] without requiring Intent-Parcelable serialization.
 *
 * [AniTrendNavKey] extends [NavKey] (which is @Serializable but not Parcelable),
 * so this holder avoids overhead of JSON serialization and handles the case
 * where the Nav3 host has not yet subscribed to [NavigationDispatcher] commands.
 *
 * Usage:
 * 1. [DeepLinkScreen] calls [PendingNavKeyHolder.post] before starting the shell.
 * 2. [MainComposeActivity] calls [PendingNavKeyHolder.consume] once in onCreate
 *    and navigates to the returned key (or defaults to [HomeNavKey]).
 */
class PendingNavKeyHolder {
    private var pending: AniTrendNavKey? = null

    /**
     * Store a pending NavKey for the next shell Activity.
     * Called from [DeepLinkScreen.onNavigateTo] after deep link resolution.
     */
    fun post(key: AniTrendNavKey) {
        pending = key
    }

    /**
     * Consume and clear the pending NavKey.
     * Called from [MainComposeActivity.onCreate] once.
     */
    fun consume(): AniTrendNavKey? = pending.also { pending = null }
}
