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

package co.anitrend.data.feed.episode.mapper

import co.anitrend.data.android.mapper.DefaultMapper
import co.anitrend.data.feed.episode.converter.EpisodeModelConverter
import co.anitrend.data.feed.episode.datasource.local.EpisodeLocalSource
import co.anitrend.data.feed.episode.entity.EpisodeEntity
import co.anitrend.data.feed.episode.model.page.EpisodePageModel

internal class EpisodeMapper(
    private val localSource: EpisodeLocalSource,
    private val converter: EpisodeModelConverter
) : DefaultMapper<EpisodePageModel, List<EpisodeEntity>>() {

    /**
     * Save [data] into your desired local source
     */
    override suspend fun persist(data: List<EpisodeEntity>) {
        localSource.upsert(data)
    }

    /**
     * Creates mapped objects and handles the database operations which may be required to map various objects,
     *
     * @param source the incoming data source type
     * @return mapped object that will be consumed by [onResponseDatabaseInsert]
     */
    override suspend fun onResponseMapFrom(
        source: EpisodePageModel
    ) = converter.convertFrom(source.channel?.items.orEmpty())
}