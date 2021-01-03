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
import co.anitrend.data.arch.FuzzyDateInt
import co.anitrend.data.shared.common.Identity
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.medialist.enums.MediaListStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(
    tableName = "media_list",
    primaryKeys = ["id"]
)
internal data class MediaListEntity(
    @ColumnInfo(name = "media_type") val mediaType: MediaType,
    @ColumnInfo(name = "advanced_scores") val advancedScores: Map<String, Float> = emptyMap(),
    @ColumnInfo(name = "custom_lists") val customLists: List<CustomList> = emptyList(),
    @ColumnInfo(name = "completed_at") val completedAt: FuzzyDateInt? = null,
    @ColumnInfo(name = "created_at") val createdAt: Long? = null,
    @ColumnInfo(name = "hidden_from_status") val hiddenFromStatus: Boolean = false,
    @ColumnInfo(name = "media_id") val mediaId: Long,
    @ColumnInfo(name = "notes") val notes: String? = null,
    @ColumnInfo(name = "priority") val priority: Int? = null,
    @ColumnInfo(name = "private") val private: Boolean = false,
    @ColumnInfo(name = "progress") val progress: Int = 0,
    @ColumnInfo(name = "progress_volumes") val progressVolumes: Int = 0,
    @ColumnInfo(name = "repeat_count") val repeatCount: Int = 0,
    @ColumnInfo(name = "score") val score: Float = 0f,
    @ColumnInfo(name = "started_at") val startedAt: FuzzyDateInt? = null,
    @ColumnInfo(name = "status") val status: MediaListStatus = MediaListStatus.PLANNING,
    @ColumnInfo(name = "updated_at") val updatedAt: Long? = null,
    @ColumnInfo(name = "user_id") val userId: Long,
    @ColumnInfo(name = "id") override val id: Long
) : Identity {

    @Serializable
    internal data class CustomList(
        @SerialName("name") val name: String,
        @SerialName("enabled") val enabled: Boolean
    )
}