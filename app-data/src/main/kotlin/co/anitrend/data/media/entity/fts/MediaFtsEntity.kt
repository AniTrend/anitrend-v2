/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.data.media.entity.fts

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import co.anitrend.data.media.entity.MediaEntity

@Entity(tableName = "media_fts")
@Fts4(contentEntity = MediaEntity::class)
internal data class MediaFtsEntity(
    @ColumnInfo(name = "title_romaji") val romaji: String,
    @ColumnInfo(name = "title_english") val english: String,
    @ColumnInfo(name = "title_original") val original: String
)