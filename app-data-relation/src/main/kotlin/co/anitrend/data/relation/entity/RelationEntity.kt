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

package co.anitrend.data.relation.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "relation",
    primaryKeys = ["anilist"]
)
data class RelationEntity(
    @ColumnInfo(name = "anidb") val aniDb: Long? = null,
    @ColumnInfo(name = "anilist") val anilist: Long = 0,
    @ColumnInfo(name = "anime-planet") val animePlanet: String? = null,
    @ColumnInfo(name = "anisearch") val aniSearch: Long? = null,
    @ColumnInfo(name = "imdb") val imdb: String? = null,
    @ColumnInfo(name = "kitsu") val kitsu: Long? = null,
    @ColumnInfo(name = "livechart") val liveChart: Long? = null,
    @ColumnInfo(name = "notify-moe") val notifyMoe: String? = null,
    @ColumnInfo(name = "themoviedb") val theMovieDb: Long? = null,
    @ColumnInfo(name = "myanimelist") val mal: Long? = null,
)