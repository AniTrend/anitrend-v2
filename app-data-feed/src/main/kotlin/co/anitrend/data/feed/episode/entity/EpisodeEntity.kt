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
package co.anitrend.data.feed.episode.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import co.anitrend.data.feed.contract.RssLocale
import co.anitrend.domain.common.entity.contract.IEntity

@Entity(
    tableName = "episode",
    primaryKeys = ["id"],
)
data class EpisodeEntity(
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "guid") val guid: String,
    @ColumnInfo(name = "media_id") val mediaId: Long,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "sub_titles") val subtitles: List<RssLocale>,
    @Embedded(prefix = "series_") val series: Series,
    @Embedded(prefix = "thumbnail_") val coverImage: CoverImage?,
    @Embedded(prefix = "available_") val availability: Availability,
    @Embedded(prefix = "info_") val about: Information,
    @ColumnInfo(name = "id") override val id: Long,
) : IEntity {
    data class CoverImage(
        @ColumnInfo(name = "large") val large: String?,
        @ColumnInfo(name = "medium") val medium: String?,
    )

    data class Availability(
        @ColumnInfo(name = "free_time") val freeTime: Long,
        @ColumnInfo(name = "premium_time") val premiumTime: Long,
    )

    data class Information(
        @ColumnInfo(name = "episode_duration") val episodeDuration: String,
        @ColumnInfo(name = "episode_title") val episodeTitle: String?,
        @ColumnInfo(name = "episode_number") val episodeNumber: String?,
    )

    data class Series(
        @ColumnInfo(name = "title") val seriesTitle: String,
        @ColumnInfo(name = "publisher") val seriesPublisher: String?,
        @ColumnInfo(name = "season") val seriesSeason: String?,
        @ColumnInfo(name = "keywords") val keywords: List<String>,
        @ColumnInfo(name = "rating") val rating: String?,
    )
}
