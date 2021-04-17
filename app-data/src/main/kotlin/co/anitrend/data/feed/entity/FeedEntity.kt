/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.data.feed.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import co.anitrend.data.core.common.Identity

internal sealed class FeedEntity : Identity {

    @Entity(tableName = "list_feed")
    data class ListFeed(
        @PrimaryKey
        override val id: Long
    ) : FeedEntity()

    @Entity(tableName = "message_feed")
    data class MessageFeed(
        @PrimaryKey
        override val id: Long
    ) : FeedEntity()

    @Entity(tableName = "reply_feed")
    data class ReplyFeed(
        @PrimaryKey
        override val id: Long
    ) : FeedEntity()

    @Entity(tableName = "text_feed")
    data class TextFeed(
        @PrimaryKey
        override val id: Long
    ) : FeedEntity()
}
