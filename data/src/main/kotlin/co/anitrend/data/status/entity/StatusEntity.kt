/*
 * Copyright (C) 2021 AniTrend
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
package co.anitrend.data.status.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import co.anitrend.data.core.common.IEntityId
import co.anitrend.data.user.entity.UserEntity
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaStatus
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.medialist.enums.MediaListStatus
import co.anitrend.domain.status.enums.StatusType
import co.anitrend.support.query.builder.annotation.EntitySchema

internal sealed class StatusEntity : IEntityId<Long> {
    /**
     * Persisted list-activity entry for a user. Includes inline media preview columns
     * to avoid requiring a separate join when displaying activities on the user profile.
     *
     * Owned by `data:status`.
     */
    @Entity(
        tableName = "list_status",
        indices = [
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
    data class ListStatus(
        @ColumnInfo(name = "user_id") val userId: Long,
        @ColumnInfo(name = "created_at") val createdAt: Long,
        @ColumnInfo(name = "sort_index") val sortIndex: Int,
        @ColumnInfo(name = "activity_status") val status: String?,
        @ColumnInfo(name = "progress") val progress: String?,
        @ColumnInfo(name = "site_url") val siteUrl: String?,
        @ColumnInfo(name = "activity_type") val type: StatusType?,
        // Inline media preview (nullable — not all activities have an associated media item)
        @ColumnInfo(name = "media_id") val mediaId: Long?,
        @ColumnInfo(name = "media_title_romaji") val mediaTitleRomaji: String?,
        @ColumnInfo(name = "media_title_english") val mediaTitleEnglish: String?,
        @ColumnInfo(name = "media_title_native") val mediaTitleNative: String?,
        @ColumnInfo(name = "media_title_user_preferred") val mediaTitleUserPreferred: String?,
        @ColumnInfo(name = "media_cover_color") val mediaCoverColor: String?,
        @ColumnInfo(name = "media_cover_large") val mediaCoverLarge: String?,
        @ColumnInfo(name = "media_cover_medium") val mediaCoverMedium: String?,
        @ColumnInfo(name = "media_type") val mediaType: MediaType?,
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
        @PrimaryKey override val id: Long,
    ) : StatusEntity()

    @Entity(tableName = "message_status")
    data class MessageStatus(
        @PrimaryKey
        override val id: Long,
    ) : StatusEntity()

    @Entity(tableName = "reply_status")
    data class ReplyStatus(
        @PrimaryKey
        override val id: Long,
    ) : StatusEntity()

    @Entity(tableName = "text_status")
    data class TextStatus(
        @PrimaryKey
        override val id: Long,
    ) : StatusEntity()
}
