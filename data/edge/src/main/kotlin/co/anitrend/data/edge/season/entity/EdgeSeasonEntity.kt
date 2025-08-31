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
package co.anitrend.data.edge.season.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import co.anitrend.data.core.common.IEntityId
import co.anitrend.support.query.builder.annotation.EntitySchema
import co.anitrend.data.edge.media.entity.EdgeMediaEntity

/**
 * Season data slice for a media.
 * Retains original table & indices; relocated from media aggregate.
 */
@Entity(
    tableName = "edge_media_season",
    indices = [
        Index(value = ["media_id", "season_number"], unique = true),
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
data class EdgeSeasonEntity(
    @ColumnInfo(name = "media_id") val mediaId: String,
    @ColumnInfo(name = "season_number") val number: Int,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "tmdb_id") val tmdbId: Long?,
    @ColumnInfo(name = "overview") val overview: String?,
    @ColumnInfo(name = "episode_count") val episodeCount: Int?,
    @ColumnInfo(name = "cover") val cover: String?,
    @ColumnInfo(name = "air_date") val airDate: Long?,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") override val id: Long = 0,
) : IEntityId<Long>
