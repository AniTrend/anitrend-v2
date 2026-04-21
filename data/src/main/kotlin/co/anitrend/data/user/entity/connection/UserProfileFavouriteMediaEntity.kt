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
 * User-scoped connection/ordering table that associates a user with their favourite media
 * (anime or manga). Carries inline media preview columns to avoid requiring a join at read
 * time, following the same pattern as [co.anitrend.data.media.entity.connection.MediaStaffConnectionEntity].
 *
 * Table: `user_profile_favourite_media`
 */
@Entity(
    tableName = "user_profile_favourite_media",
    indices = [
        Index(value = ["user_id", "media_id", "category"], unique = true),
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
internal data class UserProfileFavouriteMediaEntity(
    @ColumnInfo(name = "user_id") val userId: Long,
    @ColumnInfo(name = "media_id") val mediaId: Long,
    /** "ANIME" or "MANGA" */
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "sort_index") val sortIndex: Int,
    // Inline media preview columns (follow MediaStaffConnectionEntity flat-column pattern)
    @ColumnInfo(name = "title_romaji") val titleRomaji: String?,
    @ColumnInfo(name = "title_english") val titleEnglish: String?,
    @ColumnInfo(name = "title_native") val titleNative: String?,
    @ColumnInfo(name = "title_user_preferred") val titleUserPreferred: String?,
    @ColumnInfo(name = "cover_color") val coverColor: String?,
    @ColumnInfo(name = "cover_large") val coverLarge: String?,
    @ColumnInfo(name = "cover_medium") val coverMedium: String?,
    @ColumnInfo(name = "media_type") val type: MediaType?,
    @ColumnInfo(name = "media_format") val format: MediaFormat?,
    @ColumnInfo(name = "media_status") val status: MediaStatus?,
    @ColumnInfo(name = "episodes") val episodes: Int?,
    @ColumnInfo(name = "chapters") val chapters: Int?,
    @ColumnInfo(name = "volumes") val volumes: Int?,
    @ColumnInfo(name = "is_favourite") val isFavourite: Boolean?,
    @ColumnInfo(name = "mean_score") val meanScore: Int?,
    @ColumnInfo(name = "average_score") val averageScore: Int?,
    @ColumnInfo(name = "site_url") val siteUrl: String?,
    @ColumnInfo(name = "media_list_status") val mediaListStatus: MediaListStatus?,
    @ColumnInfo(name = "media_list_progress") val mediaListProgress: Int?,
    @ColumnInfo(name = "media_list_volume_progress") val mediaListVolumeProgress: Int?,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long = 0,
)

