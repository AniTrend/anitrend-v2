/*
 * Copyright (C) 2020  AniTrend
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
import co.anitrend.navigation.MediaRouter
import co.anitrend.navigation.NotificationRouter
import co.anitrend.navigation.ProfileRouter
import co.anitrend.navigation.SocialFeedRouter
import co.anitrend.navigation.router.NavigationRouter

/**
 * Abstract representation of a shortcut Shortcut
 */
abstract class Shortcut {
    abstract val id: String
    abstract val router: NavigationRouter
    @get:StringRes abstract val label: Int
    @get:DrawableRes abstract val icon: Int
    @get:StringRes abstract val disabledMessage: Int

    data class AiringSchedule(
        override val id: String = "airing_schedule",
        override val router: NavigationRouter = MediaRouter,
        override val label: Int,
        override val icon: Int,
        override val disabledMessage: Int
    ) : Shortcut()

    data class Notification(
        override val id: String = "notifications",
        override val router: NavigationRouter = NotificationRouter,
        override val label: Int,
        override val icon: Int,
        override val disabledMessage: Int
    ) : Shortcut()

    data class Profile(
        override val id: String = "profile",
        override val router: NavigationRouter = ProfileRouter,
        override val label: Int,
        override val icon: Int,
        override val disabledMessage: Int
    ) : Shortcut()

    data class SocialFeed(
        override val id: String = "social_feed",
        override val router: NavigationRouter = SocialFeedRouter,
        override val label: Int,
        override val icon: Int,
        override val disabledMessage: Int
    ) : Shortcut()
}