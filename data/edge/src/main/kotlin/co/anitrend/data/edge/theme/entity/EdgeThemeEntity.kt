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
package co.anitrend.data.edge.theme.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import co.anitrend.data.core.common.IEntityId
import co.anitrend.data.edge.media.entity.EdgeMediaEntity
import co.anitrend.support.query.builder.annotation.EntitySchema

@Entity(
    tableName = "edge_anime_theme",
    indices = [
        Index(value = ["media_id", "theme_id"], unique = true),
        Index(value = ["theme_id"], unique = true),
        Index(value = ["media_id"]),
    ],
    foreignKeys = [
        ForeignKey(
            entity = EdgeMediaEntity::class,
            parentColumns = ["id"],
            childColumns = ["media_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
    ],
)
@EntitySchema
data class EdgeThemeEntity(
    @ColumnInfo(name = "media_id") val mediaId: String,
    @ColumnInfo(name = "theme_id") val themeId: String,
    @ColumnInfo(name = "slug") val slug: String?,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "sequence") val sequence: Int,
    @ColumnInfo(name = "song_id") val songId: Long?,
    @ColumnInfo(name = "song_title") val songTitle: String,
    @PrimaryKey @ColumnInfo(name = "id") override val id: String,
) : IEntityId<String>

@Entity(
    tableName = "edge_anime_theme_entry",
    indices = [
        Index(value = ["theme_id", "entry_id"], unique = true),
        Index(value = ["entry_id"], unique = true),
        Index(value = ["theme_id"]),
    ],
    foreignKeys = [
        ForeignKey(
            entity = EdgeThemeEntity::class,
            parentColumns = ["theme_id"],
            childColumns = ["theme_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
    ],
)
@EntitySchema
data class EdgeThemeEntryEntity(
    @ColumnInfo(name = "theme_id") val themeId: String,
    @ColumnInfo(name = "entry_id") val entryId: String,
    @ColumnInfo(name = "episodes") val episodes: String?,
    @ColumnInfo(name = "notes") val notes: String?,
    @ColumnInfo(name = "nsfw") val nsfw: Boolean,
    @ColumnInfo(name = "spoiler") val spoiler: Boolean,
    @ColumnInfo(name = "version") val version: Int,
    @PrimaryKey @ColumnInfo(name = "id") override val id: String,
) : IEntityId<String>

@Entity(
    tableName = "edge_anime_theme_video",
    indices = [
        Index(value = ["entry_id", "video_id"], unique = true),
        Index(value = ["entry_id"]),
    ],
    foreignKeys = [
        ForeignKey(
            entity = EdgeThemeEntryEntity::class,
            parentColumns = ["entry_id"],
            childColumns = ["entry_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
    ],
)
@EntitySchema
data class EdgeThemeVideoEntity(
    @ColumnInfo(name = "entry_id") val entryId: String,
    @ColumnInfo(name = "video_id") val videoId: String,
    @ColumnInfo(name = "link") val link: String,
    @ColumnInfo(name = "resolution") val resolution: Int?,
    @ColumnInfo(name = "source") val source: String?,
    @ColumnInfo(name = "subbed") val subbed: Boolean,
    @ColumnInfo(name = "lyrics") val lyrics: Boolean,
    @ColumnInfo(name = "nc") val nc: Boolean,
    @ColumnInfo(name = "uncen") val uncen: Boolean,
    @ColumnInfo(name = "tags") val tags: String?,
    @ColumnInfo(name = "overlap") val overlap: String?,
    @ColumnInfo(name = "audio_id") val audioId: Long?,
    @ColumnInfo(name = "audio_link") val audioLink: String?,
    @PrimaryKey @ColumnInfo(name = "id") override val id: String,
) : IEntityId<String>
