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
package co.anitrend.data.edge.episode.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import co.anitrend.data.core.common.IEntityId
import co.anitrend.data.edge.season.entity.EdgeSeasonEntity
import co.anitrend.support.query.builder.annotation.EntitySchema

/**
 * Episode data slice for a media season.
 * Retains original table & indices; relocated from media aggregate.
 */
@Entity(
    tableName = "edge_media_episode",
    indices = [
        Index(value = ["media_id", "season_number", "episode_number"], unique = true),
        Index(value = ["media_id"]),
        Index(value = ["season_number"]),
    ],
    foreignKeys = [
        ForeignKey(
            entity = EdgeSeasonEntity::class,
            parentColumns = ["media_id", "season_number"],
            childColumns = ["media_id", "season_number"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
    ],
)
@EntitySchema
data class EdgeEpisodeEntity(
    @ColumnInfo(name = "media_id") val mediaId: String,
    @ColumnInfo(name = "season_number") val seasonNumber: Int,
    @ColumnInfo(name = "episode_number") val episodeNumber: Int,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "overview") val overview: String?,
    @ColumnInfo(name = "image") val image: String?,
    @ColumnInfo(name = "poster") val poster: String?,
    @ColumnInfo(name = "runtime") val runtime: Int?,
    @ColumnInfo(name = "absolute_episode_number") val absoluteEpisodeNumber: Int?,
    @ColumnInfo(name = "air_date") val airDate: Long,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") override val id: Long = 0,
) : IEntityId<Long>
