/*
 * Copyright (C) 2025 AniTrend
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
package co.anitrend.data.edge.news.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import co.anitrend.data.core.common.IEntityId
import co.anitrend.support.query.builder.annotation.EntitySchema

@Entity(
    tableName = "edge_news",
    indices = [Index(value = ["cursor"], unique = true)],
)
@EntitySchema
data class EdgeNewsEntity(
    @ColumnInfo(name = "cursor") val cursor: String,
    @ColumnInfo(name = "news_id") val newsId: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "image") val image: String?,
    @ColumnInfo(name = "source") val source: String?,
    @ColumnInfo(name = "published_at") val publishedAt: Long?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "content") val content: String?,
    @PrimaryKey(autoGenerate = true) override val id: Long? = null,
) : IEntityId<Long?>
