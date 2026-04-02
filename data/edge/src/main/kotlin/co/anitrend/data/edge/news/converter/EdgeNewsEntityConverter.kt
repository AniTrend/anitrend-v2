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
package co.anitrend.data.edge.news.converter

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.data.edge.news.entity.EdgeNewsEntity
import co.anitrend.domain.news.entity.News

internal class EdgeNewsEntityConverter(
    override val fromType: (EdgeNewsEntity) -> News = { source ->
        News(
            id = source.newsId.hashCode().toLong(),
            guid = source.newsId,
            link = source.url,
            title = source.title,
            image = source.image,
            author = source.source ?: "",
            subTitle = source.description?.take(140) ?: "",
            description = source.description,
            content = source.description ?: "",
            publishedOn = source.publishedAt,
        )
    },
    override val toType: (News) -> EdgeNewsEntity = { _ -> throw NotImplementedError() },
) : SupportConverter<EdgeNewsEntity, News>()
