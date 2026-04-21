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
package co.anitrend.data.user.entity.connection

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import co.anitrend.data.user.entity.UserEntity
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaStatus
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.medialist.enums.MediaListStatus
import co.anitrend.support.query.builder.annotation.EntitySchema

/**
 * User-scoped review preview cache. Stores the subset of review fields available from the
 * profile-feed API query (summary, score, ratings, dates, media preview). The full review
 * body, privacy flag, and user-vote rating are not available at this query level, so this
 * entity is intentionally distinct from [co.anitrend.data.review.entity.ReviewEntity].
 *
 * Inline media preview columns follow the flat-column pattern used by
 * [co.anitrend.data.media.entity.connection.MediaStaffConnectionEntity] to avoid a runtime
 * join and keep this entity self-contained.
 *
 * Table: `user_profile_review`
 */
@Entity(
    tableName = "user_profile_review",
    indices = [
        Index(value = ["user_id", "review_id"], unique = true),
        Index(value = ["user_id", "sort_index"]),
    ],
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
@EntitySchema
internal data class UserProfileReviewEntity(
    @ColumnInfo(name = "user_id") val userId: Long,
    @ColumnInfo(name = "review_id") val reviewId: Long,
    @ColumnInfo(name = "sort_index") val sortIndex: Int,
    @ColumnInfo(name = "summary") val summary: String?,
    @ColumnInfo(name = "score") val score: Int?,
    @ColumnInfo(name = "rating") val rating: Int?,
    @ColumnInfo(name = "rating_amount") val ratingAmount: Int?,
    @ColumnInfo(name = "site_url") val siteUrl: String?,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long,
    @ColumnInfo(name = "review_media_id") val mediaId: Long,
    @ColumnInfo(name = "review_media_type") val mediaType: MediaType?,
    // Inline media preview columns (flat, following MediaStaffConnectionEntity pattern)
    @ColumnInfo(name = "media_title_romaji") val mediaTitleRomaji: String?,
    @ColumnInfo(name = "media_title_english") val mediaTitleEnglish: String?,
    @ColumnInfo(name = "media_title_native") val mediaTitleNative: String?,
    @ColumnInfo(name = "media_title_user_preferred") val mediaTitleUserPreferred: String?,
    @ColumnInfo(name = "media_cover_color") val mediaCoverColor: String?,
    @ColumnInfo(name = "media_cover_large") val mediaCoverLarge: String?,
    @ColumnInfo(name = "media_cover_medium") val mediaCoverMedium: String?,
    @ColumnInfo(name = "media_type") val mediaEntityType: MediaType?,
    @ColumnInfo(name = "media_format") val mediaFormat: MediaFormat?,
    @ColumnInfo(name = "media_status") val mediaStatus: MediaStatus?,
    @ColumnInfo(name = "media_episodes") val mediaEpisodes: Int?,
    @ColumnInfo(name = "media_chapters") val mediaChapters: Int?,
    @ColumnInfo(name = "media_volumes") val mediaVolumes: Int?,
    @ColumnInfo(name = "media_is_favourite") val mediaIsFavourite: Boolean?,
    @ColumnInfo(name = "media_mean_score") val mediaMeanScore: Int?,
    @ColumnInfo(name = "media_average_score") val mediaAverageScore: Int?,
    @ColumnInfo(name = "media_site_url") val mediaSiteUrl: String?,
    @ColumnInfo(name = "media_list_status") val mediaListStatus: MediaListStatus?,
    @ColumnInfo(name = "media_list_progress") val mediaListProgress: Int?,
    @ColumnInfo(name = "media_list_volume_progress") val mediaListVolumeProgress: Int?,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long = 0,
)
