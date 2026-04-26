/*
 * Copyright (C) 2020 AniTrend
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
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import co.anitrend.data.core.common.IEntityId
import co.anitrend.data.user.entity.UserEntity

@Entity(
    tableName = "user_statistic",
    indices = [Index(value = ["user_id"])],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
            childColumns = ["user_id"],
            parentColumns = ["id"],
        ),
    ],
)
internal data class UserWithStatisticEntity(
    @Embedded(prefix = "statistic_") val statistic: Statistic,
    @ColumnInfo(name = "user_id") val userId: Long,
    @PrimaryKey @ColumnInfo(name = "id") override val id: Long,
) : IEntityId<Long> {
    data class Statistic(
        @ColumnInfo(name = "anime_count") val animeCount: Int?,
        @ColumnInfo(name = "anime_mean_score") val animeMeanScore: Float?,
        @ColumnInfo(name = "anime_standard_deviation") val animeStandardDeviation: Float?,
        @ColumnInfo(name = "anime_minutes_watched") val animeMinutesWatched: Int?,
        @ColumnInfo(name = "anime_episodes_watched") val animeEpisodesWatched: Int?,
        @ColumnInfo(name = "manga_count") val mangaCount: Int?,
        @ColumnInfo(name = "manga_mean_score") val mangaMeanScore: Float?,
        @ColumnInfo(name = "manga_standard_deviation") val mangaStandardDeviation: Float?,
        @ColumnInfo(name = "manga_chapters_read") val mangaChaptersRead: Int?,
        @ColumnInfo(name = "manga_volumes_read") val mangaVolumesRead: Int?,
    )
}
