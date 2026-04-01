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
import co.anitrend.data.edge.news.model.remote.EdgeNewsConnectionModel

internal class EdgeNewsModelConverter(
    override val fromType: (List<EdgeNewsConnectionModel.News>) -> List<EdgeNewsEntity> = { items ->
        items.map { item ->
            EdgeNewsEntity(
                cursor = item.id,
                newsId = item.id,
                title = item.title,
                url = item.link,
                image = item.image,
                source = item.category ?: item.area ?: item.genre ?: item.language,
                publishedAt = item.publishedOn?.toLong(),
                description = item.description.ifBlank { item.content },
            )
        }
    },
    override val toType: (List<EdgeNewsEntity>) -> List<EdgeNewsConnectionModel.News> = { _ ->
        throw NotImplementedError()
    },
) : SupportConverter<List<EdgeNewsConnectionModel.News>, List<EdgeNewsEntity>>()
