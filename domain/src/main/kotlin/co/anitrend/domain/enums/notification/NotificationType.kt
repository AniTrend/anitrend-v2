/*
 * Copyright (C) 2019  AniTrend
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

package co.anitrend.domain.enums.notification

import co.anitrend.domain.common.enum.IGraphEnum

/**
 * Notification type enum
 */
enum class NotificationType(override val value: String) : IGraphEnum {
    /** A user has liked your activity */
    ACTIVITY_LIKE("ACTIVITY_LIKE"),
    /** A user has mentioned you in their activity */
    ACTIVITY_MENTION("ACTIVITY_MENTION"),
    /** A user has sent you message */
    ACTIVITY_MESSAGE("ACTIVITY_MESSAGE"),
    /** A user has replied to your activity */
    ACTIVITY_REPLY("ACTIVITY_REPLY"),
    /** A user has liked your activity reply */
    ACTIVITY_REPLY_LIKE("ACTIVITY_REPLY_LIKE"),
    /** A user has replied to activity you have also replied to */
    ACTIVITY_REPLY_SUBSCRIBED("ACTIVITY_REPLY_SUBSCRIBED"),
    /** An anime you are currently watching has aired */
    AIRING("AIRING"),
    /** A user has followed you */
    FOLLOWING("FOLLOWING"),
    /** A new anime or manga has been added to the site where its related media is on the user's list */
    RELATED_MEDIA_ADDITION("RELATED_MEDIA_ADDITION"),
    /** A user has liked your forum comment */
    THREAD_COMMENT_LIKE("THREAD_COMMENT_LIKE"),
    /** A user has mentioned you in a forum comment */
    THREAD_COMMENT_MENTION("THREAD_COMMENT_MENTION"),
    /** A user has replied to your forum comment */
    THREAD_COMMENT_REPLY("THREAD_COMMENT_REPLY"),
    /** A user has liked your forum thread */
    THREAD_LIKE("THREAD_LIKE"),
    /** A user has commented in one of your subscribed forum threads */
    THREAD_SUBSCRIBED("THREAD_SUBSCRIBED")
}