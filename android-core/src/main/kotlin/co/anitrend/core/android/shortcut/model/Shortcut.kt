/*
 * Copyright (C) 2020 AniTrend
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
package co.anitrend.core.android.shortcut.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import co.anitrend.core.android.R
import co.anitrend.navigation.AiringRouter
import co.anitrend.navigation.FeedRouter
import co.anitrend.navigation.MediaListRouter
import co.anitrend.navigation.NotificationRouter
import co.anitrend.navigation.ProfileRouter
import co.anitrend.navigation.SearchRouter
import co.anitrend.navigation.model.NavPayload
import co.anitrend.navigation.router.NavigationRouter

/**
 * Abstract representation of a shortcut Shortcut
 */
sealed class Shortcut {
    abstract val id: String
    abstract val router: NavigationRouter
    abstract val navPayload: NavPayload?

    @get:StringRes abstract val label: Int

    @get:DrawableRes abstract val icon: Int

    @get:StringRes abstract val disabledMessage: Int

    data class AiringSchedule(
        override val id: String = "airing_schedule",
        override val router: NavigationRouter = AiringRouter,
        override val label: Int = R.string.shortcut_label_airing_schedule,
        override val icon: Int = R.drawable.ic_shortcut_airing_schedule,
        override val disabledMessage: Int = R.string.shortcut_label_airing_schedule,
        override val navPayload: NavPayload? = null,
    ) : Shortcut()

    data class AnimeList(
        override val id: String = "anime_list",
        override val router: NavigationRouter = MediaListRouter,
        override val label: Int = R.string.shortcut_label_anime_list,
        override val icon: Int = R.drawable.ic_shortcut_anime_list,
        override val disabledMessage: Int = R.string.shortcut_label_anime_list,
        override val navPayload: NavPayload? = null,
    ) : Shortcut()

    data class MangaList(
        override val id: String = "manga_list",
        override val router: NavigationRouter = MediaListRouter,
        override val label: Int = R.string.shortcut_label_manga_list,
        override val icon: Int = R.drawable.ic_shortcut_manga_list,
        override val disabledMessage: Int = R.string.shortcut_label_manga_list,
        override val navPayload: NavPayload? = null,
    ) : Shortcut()

    data class Notification(
        override val id: String = "notifications",
        override val router: NavigationRouter = NotificationRouter,
        override val label: Int = R.string.shortcut_label_notifications,
        override val icon: Int = R.drawable.ic_shortcut_notifications,
        override val disabledMessage: Int = R.string.shortcut_label_notifications,
        override val navPayload: NavPayload? = null,
    ) : Shortcut()

    data class Profile(
        override val id: String = "profile",
        override val router: NavigationRouter = ProfileRouter,
        override val label: Int = R.string.shortcut_label_profile,
        override val icon: Int = R.drawable.ic_shortcut_profile,
        override val disabledMessage: Int = R.string.shortcut_label_profile,
        override val navPayload: NavPayload? = null,
    ) : Shortcut()

    data class Feed(
        override val id: String = "feed",
        override val router: NavigationRouter = FeedRouter,
        override val label: Int = R.string.shortcut_label_activity,
        override val icon: Int = R.drawable.ic_shortcut_activity,
        override val disabledMessage: Int = R.string.shortcut_label_activity,
        override val navPayload: NavPayload? = null,
    ) : Shortcut()

    data class Search(
        override val id: String = "search",
        override val router: NavigationRouter = SearchRouter,
        override val label: Int = R.string.shortcut_label_search,
        override val icon: Int = R.drawable.ic_shortcut_search,
        override val disabledMessage: Int = R.string.shortcut_label_search,
        override val navPayload: NavPayload? = null,
    ) : Shortcut()
}
