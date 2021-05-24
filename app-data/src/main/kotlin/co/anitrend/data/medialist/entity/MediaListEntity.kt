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

package co.anitrend.data.medialist.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import co.anitrend.data.common.FuzzyDateInt
import co.anitrend.data.core.common.Identity
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.medialist.enums.MediaListStatus
import co.anitrend.support.query.builder.annotation.EntitySchema

@Entity(
    tableName = "media_list",
    primaryKeys = ["id"]
)
@EntitySchema
internal data class MediaListEntity(
    @ColumnInfo(name = "media_type") val mediaType: MediaType,
    @ColumnInfo(name = "completed_at") val completedAt: FuzzyDateInt?,
    @ColumnInfo(name = "created_at") val createdAt: Long?,
    @ColumnInfo(name = "hidden_from_status") val hiddenFromStatus: Boolean,
    @ColumnInfo(name = "media_id") val mediaId: Long,
    @ColumnInfo(name = "notes") val notes: String?,
    @ColumnInfo(name = "priority") val priority: Int?,
    @ColumnInfo(name = "hidden") val hidden: Boolean,
    @ColumnInfo(name = "progress") val progress: Int,
    @ColumnInfo(name = "progress_volumes") val progressVolumes: Int,
    @ColumnInfo(name = "repeat_count") val repeatCount: Int,
    @ColumnInfo(name = "score") val score: Float,
    @ColumnInfo(name = "started_at") val startedAt: FuzzyDateInt?,
    @ColumnInfo(name = "status") val status: MediaListStatus,
    @ColumnInfo(name = "updated_at") val updatedAt: Long?,
    @ColumnInfo(name = "user_id") val userId: Long,
    @ColumnInfo(name = "id") override val id: Long
) : Identity