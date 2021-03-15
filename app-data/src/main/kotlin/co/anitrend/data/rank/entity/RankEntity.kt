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

package co.anitrend.data.rank.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.data.shared.common.Identity
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaRankType
import co.anitrend.domain.media.enums.MediaSeason

@Entity(
    tableName = "rank",
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
internal data class RankEntity(
    @ColumnInfo(name = "media_id") val mediaId: Long,
    @ColumnInfo(name = "all_time") val allTime: Boolean?,
    @ColumnInfo(name = "context") val context: String,
    @ColumnInfo(name = "format") val format: MediaFormat,
    @ColumnInfo(name = "rank") val rank: Int,
    @ColumnInfo(name = "season") val season: MediaSeason?,
    @ColumnInfo(name = "type") val type: MediaRankType,
    @ColumnInfo(name = "year") val year: Int?,
    @ColumnInfo(name = "id") override val id: Long
) : Identity