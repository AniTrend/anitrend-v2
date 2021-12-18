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

package co.anitrend.data.status.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import co.anitrend.data.core.common.Identity

internal sealed class StatusEntity : Identity {

    @Entity(tableName = "list_status")
    data class ListStatus(
        @PrimaryKey
        override val id: Long
    ) : StatusEntity()

    @Entity(tableName = "message_status")
    data class MessageStatus(
        @PrimaryKey
        override val id: Long
    ) : StatusEntity()

    @Entity(tableName = "reply_status")
    data class ReplyStatus(
        @PrimaryKey
        override val id: Long
    ) : StatusEntity()

    @Entity(tableName = "text_status")
    data class TextStatus(
        @PrimaryKey
        override val id: Long
    ) : StatusEntity()
}
