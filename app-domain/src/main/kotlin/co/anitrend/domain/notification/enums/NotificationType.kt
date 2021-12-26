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

package co.anitrend.domain.notification.enums

import co.anitrend.domain.common.enums.contract.IAliasable

/**
 * Notification type enum
 */
enum class NotificationType(override val alias: CharSequence) : IAliasable {
    /** A user has liked your activity */
    ACTIVITY_LIKE("Activity like"),
    /** A user has mentioned you in their activity */
    ACTIVITY_MENTION("Activity mention"),
    /** A user has sent you message */
    ACTIVITY_MESSAGE("Activity message"),
    /** A user has replied to your activity */
    ACTIVITY_REPLY("Activity reply"),
    /** A user has liked your activity reply */
    ACTIVITY_REPLY_LIKE("Activity reply like"),
    /** A user has replied to activity you have also replied to */
    ACTIVITY_REPLY_SUBSCRIBED("Activity reply subscribed"),
    /** An anime you are currently watching has aired */
    AIRING("Airing"),
    /** A user has followed you */
    FOLLOWING("Following"),
    /** An anime or manga has had a data change that affects how a user may track it in their lists */
    MEDIA_DATA_CHANGE("Media data change"),
    /** An anime or manga on the user's list has been deleted from the site */
    MEDIA_DELETION("Media deletion"),
    /** Anime or manga entries on the user's list have been merged into a single entry */
    MEDIA_MERGE("Media merge"),
    /** A new anime or manga has been added to the site where its related media is on the user's list */
    RELATED_MEDIA_ADDITION("Related media addition"),
    /** A user has liked your forum comment */
    THREAD_COMMENT_LIKE("Thread comment like"),
    /** A user has mentioned you in a forum comment */
    THREAD_COMMENT_MENTION("Thread comment mention"),
    /** A user has replied to your forum comment */
    THREAD_COMMENT_REPLY("Thread comment reply"),
    /** A user has liked your forum thread */
    THREAD_LIKE("Thread like"),
    /** A user has commented in one of your subscribed forum threads */
    THREAD_SUBSCRIBED("Thread subscribed")
}