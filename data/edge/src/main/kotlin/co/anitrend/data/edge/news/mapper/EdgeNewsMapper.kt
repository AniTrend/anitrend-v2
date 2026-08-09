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
package co.anitrend.data.edge.news.mapper

import co.anitrend.data.android.mapper.DefaultMapper
import co.anitrend.data.edge.graphql.NewsConnectionData
import co.anitrend.data.edge.news.converter.EdgeNewsModelConverter
import co.anitrend.data.edge.news.datasource.local.EdgeNewsLocalSource
import co.anitrend.data.edge.news.entity.EdgeNewsEntity

internal class EdgeNewsMapper(
    private val localSource: EdgeNewsLocalSource,
    private val converter: EdgeNewsModelConverter,
) : DefaultMapper<NewsConnectionData, List<EdgeNewsEntity>>() {
    override suspend fun onResponseMapFrom(source: NewsConnectionData): List<EdgeNewsEntity> =
        converter.convertFrom(
            source.news
                ?.data
                .orEmpty()
                .filterNotNull(),
        )

    override suspend fun persist(data: List<EdgeNewsEntity>) {
        localSource.upsert(data)
    }
}
