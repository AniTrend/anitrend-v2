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

package co.anitrend.data.news.entity

import androidx.room.ColumnInfo
import androidx.room.Entity


@Entity(
    tableName = "news",
    primaryKeys = ["id"]
)
internal data class NewsEntity(
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "image") val image: String?,
    @ColumnInfo(name = "author") val author: String,
    @ColumnInfo(name = "sub_title") val subTitle: String,
    @ColumnInfo(name = "original_link") val originalLink: String,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "published_on") val publishedOn: Long?
)