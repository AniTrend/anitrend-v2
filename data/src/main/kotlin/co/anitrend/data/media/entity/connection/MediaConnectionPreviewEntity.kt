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
package co.anitrend.data.media.entity.connection

import androidx.room.ColumnInfo
import androidx.room.Embedded
import co.anitrend.data.common.FuzzyDateInt
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaStatus
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.medialist.enums.MediaListStatus

internal data class MediaConnectionPreviewEntity(
    @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "type") val type: MediaType,
    @ColumnInfo(name = "format") val format: MediaFormat?,
    @ColumnInfo(name = "status") val status: MediaStatus?,
    @ColumnInfo(name = "start_date") val startDate: FuzzyDateInt,
    @ColumnInfo(name = "episodes") val episodes: Int,
    @ColumnInfo(name = "chapters") val chapters: Int,
    @ColumnInfo(name = "volumes") val volumes: Int,
    @ColumnInfo(name = "is_favourite") val isFavourite: Boolean,
    @ColumnInfo(name = "mean_score") val meanScore: Int,
    @ColumnInfo(name = "average_score") val averageScore: Int,
    @ColumnInfo(name = "personal_score") val personalScore: Float?,
    @ColumnInfo(name = "next_airing_at") val nextAiringAt: Long?,
    @ColumnInfo(name = "next_airing_episode") val nextAiringEpisode: Int?,
    @ColumnInfo(name = "next_airing_id") val nextAiringId: Long?,
    @Embedded(prefix = "image_") val image: Image,
    @Embedded(prefix = "title_") val title: Title,
    @Embedded(prefix = "media_list_") val mediaList: MediaListSummary?,
) {
    internal data class Image(
        @ColumnInfo(name = "color") val color: String?,
        @ColumnInfo(name = "large") val large: String?,
        @ColumnInfo(name = "medium") val medium: String?,
    )

    internal data class Title(
        @ColumnInfo(name = "english") val english: String?,
        @ColumnInfo(name = "native") val nativeTitle: String?,
        @ColumnInfo(name = "romaji") val romaji: String?,
        @ColumnInfo(name = "user_preferred") val userPreferred: String?,
    )

    internal data class MediaListSummary(
        @ColumnInfo(name = "status") val status: MediaListStatus?,
        @ColumnInfo(name = "notes") val notes: String?,
    )
}
