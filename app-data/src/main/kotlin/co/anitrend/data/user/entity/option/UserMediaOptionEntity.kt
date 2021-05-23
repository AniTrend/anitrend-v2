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

package co.anitrend.data.user.entity.option

import androidx.room.*
import co.anitrend.data.core.common.Identity
import co.anitrend.data.user.entity.UserEntity
import co.anitrend.domain.medialist.enums.ScoreFormat

@Entity(
    tableName = "user_media_option",
    indices = [
        Index(
            value = ["user_id"],
            unique = true
        )
    ],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
            childColumns = ["user_id"],
            parentColumns = ["id"]
        )
    ]
)
internal data class UserMediaOptionEntity(
    @ColumnInfo(name = "user_id") val userId: Long,
    @ColumnInfo(name = "score_format") val scoreFormat: ScoreFormat,
    @ColumnInfo(name = "list_row_order") val rowOrder: String?,
    @Embedded(prefix = "anime_") val anime: MediaOption,
    @Embedded(prefix = "manga_") val manga: MediaOption,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") override val id: Long = 0
) : Identity {

    data class MediaOption(
        @ColumnInfo(name = "custom_lists") val customLists: List<String>,
        @ColumnInfo(name = "section_order") val sectionOrder: List<String>,
        @ColumnInfo(name = "advanced_scoring") val advancedScoring: List<String>,
        @ColumnInfo(name = "advanced_scoring_enabled") val advancedScoringEnabled: Boolean,
        @ColumnInfo(name = "split_completed_section_by_format") val splitCompletedSectionByFormat: Boolean
    )
}