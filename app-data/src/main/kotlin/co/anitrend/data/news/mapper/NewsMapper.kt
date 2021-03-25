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

package co.anitrend.data.news.mapper

import co.anitrend.data.arch.mapper.DefaultMapper
import co.anitrend.data.arch.railway.OutCome
import co.anitrend.data.arch.railway.extension.evaluate
import co.anitrend.data.arch.railway.extension.otherwise
import co.anitrend.data.arch.railway.extension.then
import co.anitrend.data.news.converter.NewsModelConverter
import co.anitrend.data.news.datasource.local.NewsLocalSource
import co.anitrend.data.news.entity.NewsEntity
import co.anitrend.data.news.model.page.NewsPageModel

internal class NewsMapper(
    private val localSource: NewsLocalSource,
    private val converter: NewsModelConverter
) : DefaultMapper<NewsPageModel, List<NewsEntity>>() {

    /**
     * Save [data] into your desired local source
     */
    override suspend fun persist(data: List<NewsEntity>) {
        localSource.upsert(data)
    }

    /**
     * Creates mapped objects and handles the database operations which may be required to map various objects,
     *
     * @param source the incoming data source type
     * @return mapped object that will be consumed by [onResponseDatabaseInsert]
     */
    override suspend fun onResponseMapFrom(
        source: NewsPageModel
    ) = converter.convertFrom(source.channel?.items.orEmpty())
}