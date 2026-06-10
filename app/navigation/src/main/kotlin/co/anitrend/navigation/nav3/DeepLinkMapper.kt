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

import android.content.Intent
import android.net.Uri

/**
 * Maps external deep-link and notification intents to Nav3 keys.
 *
 * Activities for deep links, share targets, notification pending intents,
 * and auth callbacks should remain as Android entry points but translate
 * external input into Nav3 keys and hand off to the Compose host.
 *
 * Example flow:
 *   Deep link URI -> DeepLinkMapper -> AniTrendNavKey -> dispatcher.navigate()
 */
object DeepLinkMapper {
    /**
     * Resolves a deep-link intent to a Nav3 key, or null if unrecognised.
     */
    fun resolve(deepLink: Uri): AniTrendNavKey? =
        when {
            deepLink.host == "anilist.co" -> resolveAniListLink(deepLink)
            deepLink.scheme == "anitrend" -> resolveAppSchemeLink(deepLink)
            else -> null
        }

    /**
     * Resolves notification tap intents to Nav3 keys.
     */
    fun resolveNotification(intent: Intent): AniTrendNavKey? {
        val type = intent.getStringExtra("notification_type") ?: return null
        val mediaId = intent.getLongExtra("media_id", -1L)
        val userId = intent.getLongExtra("user_id", -1L)

        return when (type) {
            "media" -> if (mediaId > 0) MediaNavKey(mediaId) else null
            "user" -> if (userId > 0) ProfileNavKey(userId) else null
            "news" -> NewsNavKey
            "episode" -> EpisodesNavKey
            "review" -> ReviewsNavKey
            else -> null
        }
    }

    private fun resolveAniListLink(uri: Uri): AniTrendNavKey? {
        val segments = uri.pathSegments
        return when {
            segments.size >= 2 && segments[0] == "anime" ->
                segments[1].toLongOrNull()?.let { MediaNavKey(it) }
            segments.size >= 2 && segments[0] == "manga" ->
                segments[1].toLongOrNull()?.let { MediaNavKey(it) }
            segments.size >= 2 && segments[0] == "user" ->
                segments[1].toLongOrNull()?.let { ProfileNavKey(it) }
            else -> null
        }
    }

    private fun resolveAppSchemeLink(uri: Uri): AniTrendNavKey? {
        val host = uri.host ?: return null
        return when (host) {
            "about" -> AboutNavKey
            "settings" -> SettingsNavKey()
            "discover" -> DiscoverNavKey
            "news" -> NewsNavKey
            "home" -> HomeNavKey
            "airing" -> AiringNavKey
            else -> null
        }
    }
}
