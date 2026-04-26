/*
 * Copyright (C) 2026 AniTrend
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
package co.anitrend.data.user.entity.statistic

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import co.anitrend.data.common.CountryCode
import co.anitrend.data.core.common.IEntityId
import co.anitrend.data.staff.entity.StaffEntity
import co.anitrend.data.studio.entity.StudioEntity
import co.anitrend.data.tag.entity.TagEntity
import co.anitrend.data.user.entity.UserEntity
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.medialist.enums.MediaListStatus

internal interface UserStatisticValueRow {
    val userId: Long
    val mediaType: MediaType
    val count: Int
    val meanScore: Float
    val mediaIds: List<Long>
    val trackedAmount: Int
}

@Entity(
    tableName = "user_statistic_country",
    indices = [Index(value = ["user_id", "media_type", "country"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
    ],
)
internal data class UserStatisticCountryEntity(
    @ColumnInfo(name = "country") val country: CountryCode,
    @ColumnInfo(name = "user_id") override val userId: Long,
    @ColumnInfo(name = "media_type") override val mediaType: MediaType,
    @ColumnInfo(name = "count") override val count: Int,
    @ColumnInfo(name = "mean_score") override val meanScore: Float,
    @ColumnInfo(name = "media_ids") override val mediaIds: List<Long>,
    @ColumnInfo(name = "tracked_amount") override val trackedAmount: Int,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") override val id: Long = 0,
) : IEntityId<Long>,
    UserStatisticValueRow

@Entity(
    tableName = "user_statistic_format",
    indices = [Index(value = ["user_id", "media_type", "format"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
    ],
)
internal data class UserStatisticFormatEntity(
    @ColumnInfo(name = "format") val format: MediaFormat,
    @ColumnInfo(name = "user_id") override val userId: Long,
    @ColumnInfo(name = "media_type") override val mediaType: MediaType,
    @ColumnInfo(name = "count") override val count: Int,
    @ColumnInfo(name = "mean_score") override val meanScore: Float,
    @ColumnInfo(name = "media_ids") override val mediaIds: List<Long>,
    @ColumnInfo(name = "tracked_amount") override val trackedAmount: Int,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") override val id: Long = 0,
) : IEntityId<Long>,
    UserStatisticValueRow

@Entity(
    tableName = "user_statistic_genre",
    indices = [Index(value = ["user_id", "media_type", "genre"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
    ],
)
internal data class UserStatisticGenreEntity(
    @ColumnInfo(name = "genre") val genre: String,
    @ColumnInfo(name = "user_id") override val userId: Long,
    @ColumnInfo(name = "media_type") override val mediaType: MediaType,
    @ColumnInfo(name = "count") override val count: Int,
    @ColumnInfo(name = "mean_score") override val meanScore: Float,
    @ColumnInfo(name = "media_ids") override val mediaIds: List<Long>,
    @ColumnInfo(name = "tracked_amount") override val trackedAmount: Int,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") override val id: Long = 0,
) : IEntityId<Long>,
    UserStatisticValueRow

@Entity(
    tableName = "user_statistic_length",
    indices = [Index(value = ["user_id", "media_type", "length"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
    ],
)
internal data class UserStatisticLengthEntity(
    @ColumnInfo(name = "length") val length: String?,
    @ColumnInfo(name = "user_id") override val userId: Long,
    @ColumnInfo(name = "media_type") override val mediaType: MediaType,
    @ColumnInfo(name = "count") override val count: Int,
    @ColumnInfo(name = "mean_score") override val meanScore: Float,
    @ColumnInfo(name = "media_ids") override val mediaIds: List<Long>,
    @ColumnInfo(name = "tracked_amount") override val trackedAmount: Int,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") override val id: Long = 0,
) : IEntityId<Long>,
    UserStatisticValueRow

@Entity(
    tableName = "user_statistic_release_year",
    indices = [Index(value = ["user_id", "media_type", "release_year"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
    ],
)
internal data class UserStatisticReleaseYearEntity(
    @ColumnInfo(name = "release_year") val releaseYear: Int,
    @ColumnInfo(name = "user_id") override val userId: Long,
    @ColumnInfo(name = "media_type") override val mediaType: MediaType,
    @ColumnInfo(name = "count") override val count: Int,
    @ColumnInfo(name = "mean_score") override val meanScore: Float,
    @ColumnInfo(name = "media_ids") override val mediaIds: List<Long>,
    @ColumnInfo(name = "tracked_amount") override val trackedAmount: Int,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") override val id: Long = 0,
) : IEntityId<Long>,
    UserStatisticValueRow

@Entity(
    tableName = "user_statistic_score",
    indices = [Index(value = ["user_id", "media_type", "score"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
    ],
)
internal data class UserStatisticScoreEntity(
    @ColumnInfo(name = "score") val score: Int,
    @ColumnInfo(name = "user_id") override val userId: Long,
    @ColumnInfo(name = "media_type") override val mediaType: MediaType,
    @ColumnInfo(name = "count") override val count: Int,
    @ColumnInfo(name = "mean_score") override val meanScore: Float,
    @ColumnInfo(name = "media_ids") override val mediaIds: List<Long>,
    @ColumnInfo(name = "tracked_amount") override val trackedAmount: Int,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") override val id: Long = 0,
) : IEntityId<Long>,
    UserStatisticValueRow

@Entity(
    tableName = "user_statistic_start_year",
    indices = [Index(value = ["user_id", "media_type", "start_year"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
    ],
)
internal data class UserStatisticStartYearEntity(
    @ColumnInfo(name = "start_year") val startYear: Int,
    @ColumnInfo(name = "user_id") override val userId: Long,
    @ColumnInfo(name = "media_type") override val mediaType: MediaType,
    @ColumnInfo(name = "count") override val count: Int,
    @ColumnInfo(name = "mean_score") override val meanScore: Float,
    @ColumnInfo(name = "media_ids") override val mediaIds: List<Long>,
    @ColumnInfo(name = "tracked_amount") override val trackedAmount: Int,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") override val id: Long = 0,
) : IEntityId<Long>,
    UserStatisticValueRow

@Entity(
    tableName = "user_statistic_status",
    indices = [Index(value = ["user_id", "media_type", "status"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
    ],
)
internal data class UserStatisticStatusEntity(
    @ColumnInfo(name = "status") val status: MediaListStatus,
    @ColumnInfo(name = "user_id") override val userId: Long,
    @ColumnInfo(name = "media_type") override val mediaType: MediaType,
    @ColumnInfo(name = "count") override val count: Int,
    @ColumnInfo(name = "mean_score") override val meanScore: Float,
    @ColumnInfo(name = "media_ids") override val mediaIds: List<Long>,
    @ColumnInfo(name = "tracked_amount") override val trackedAmount: Int,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") override val id: Long = 0,
) : IEntityId<Long>,
    UserStatisticValueRow

@Entity(
    tableName = "user_statistic_staff",
    indices = [Index(value = ["user_id", "media_type", "staff_id"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = StaffEntity::class,
            parentColumns = ["id"],
            childColumns = ["staff_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
    ],
)
internal data class UserStatisticStaffEntity(
    @ColumnInfo(name = "staff_id") val staffId: Long,
    @ColumnInfo(name = "user_id") override val userId: Long,
    @ColumnInfo(name = "media_type") override val mediaType: MediaType,
    @ColumnInfo(name = "count") override val count: Int,
    @ColumnInfo(name = "mean_score") override val meanScore: Float,
    @ColumnInfo(name = "media_ids") override val mediaIds: List<Long>,
    @ColumnInfo(name = "tracked_amount") override val trackedAmount: Int,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") override val id: Long = 0,
) : IEntityId<Long>,
    UserStatisticValueRow

@Entity(
    tableName = "user_statistic_studio",
    indices = [Index(value = ["user_id", "media_type", "studio_id"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = StudioEntity::class,
            parentColumns = ["id"],
            childColumns = ["studio_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
    ],
)
internal data class UserStatisticStudioEntity(
    @ColumnInfo(name = "studio_id") val studioId: Long,
    @ColumnInfo(name = "user_id") override val userId: Long,
    @ColumnInfo(name = "media_type") override val mediaType: MediaType,
    @ColumnInfo(name = "count") override val count: Int,
    @ColumnInfo(name = "mean_score") override val meanScore: Float,
    @ColumnInfo(name = "media_ids") override val mediaIds: List<Long>,
    @ColumnInfo(name = "tracked_amount") override val trackedAmount: Int,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") override val id: Long = 0,
) : IEntityId<Long>,
    UserStatisticValueRow

@Entity(
    tableName = "user_statistic_tag",
    indices = [Index(value = ["user_id", "media_type", "tag_id"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = TagEntity::class,
            parentColumns = ["id"],
            childColumns = ["tag_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
    ],
)
internal data class UserStatisticTagEntity(
    @ColumnInfo(name = "tag_id") val tagId: Long,
    @ColumnInfo(name = "user_id") override val userId: Long,
    @ColumnInfo(name = "media_type") override val mediaType: MediaType,
    @ColumnInfo(name = "count") override val count: Int,
    @ColumnInfo(name = "mean_score") override val meanScore: Float,
    @ColumnInfo(name = "media_ids") override val mediaIds: List<Long>,
    @ColumnInfo(name = "tracked_amount") override val trackedAmount: Int,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") override val id: Long = 0,
) : IEntityId<Long>,
    UserStatisticValueRow

@Entity(
    tableName = "user_statistic_voice_actor",
    indices = [Index(value = ["user_id", "media_type", "staff_id"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = StaffEntity::class,
            parentColumns = ["id"],
            childColumns = ["staff_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
    ],
)
internal data class UserStatisticVoiceActorEntity(
    @ColumnInfo(name = "staff_id") val staffId: Long,
    @ColumnInfo(name = "user_id") override val userId: Long,
    @ColumnInfo(name = "media_type") override val mediaType: MediaType,
    @ColumnInfo(name = "count") override val count: Int,
    @ColumnInfo(name = "mean_score") override val meanScore: Float,
    @ColumnInfo(name = "media_ids") override val mediaIds: List<Long>,
    @ColumnInfo(name = "tracked_amount") override val trackedAmount: Int,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") override val id: Long = 0,
) : IEntityId<Long>,
    UserStatisticValueRow
