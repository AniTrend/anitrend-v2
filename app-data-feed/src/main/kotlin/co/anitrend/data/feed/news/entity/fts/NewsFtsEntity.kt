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

package co.anitrend.data.feed.news.entity.fts

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import co.anitrend.data.feed.news.entity.NewsEntity

@Entity(tableName = "news_fts")
@Fts4(contentEntity = NewsEntity::class)
data class NewsFtsEntity(
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "sub_title") val subTitle: String,
    @ColumnInfo(name = "description") val description: String?
)