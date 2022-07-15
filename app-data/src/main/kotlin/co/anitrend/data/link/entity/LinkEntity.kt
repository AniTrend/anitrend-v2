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

package co.anitrend.data.link.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import co.anitrend.data.core.common.Identity
import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.domain.media.enums.ExternalLinkType
import co.anitrend.support.query.builder.annotation.EntitySchema

@Entity(
    tableName = "link",
    primaryKeys = ["id"],
    foreignKeys = [
        ForeignKey(
            entity = MediaEntity::class,
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
            childColumns = ["media_id"],
            parentColumns = ["id"]
        )
    ],
    indices = [Index(value = ["media_id"])]
)
@EntitySchema
internal data class LinkEntity(
    @ColumnInfo(name = "media_id") val mediaId: Long,
    @ColumnInfo(name = "color") val color: String?,
    @ColumnInfo(name = "icon") val icon: String?,
    @ColumnInfo(name = "is_disabled") val isDisabled: Boolean?,
    @ColumnInfo(name = "language") val language: String?,
    @ColumnInfo(name = "notes") val notes: String?,
    @ColumnInfo(name = "site_id") val siteId: Int?,
    @ColumnInfo(name = "link_type") val linkType: ExternalLinkType?,
    @ColumnInfo(name = "site") val site: String,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "id") override val id: Long
) : Identity