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

package co.anitrend.data.jikan.media.mapper

import co.anitrend.data.android.mapper.DefaultMapper
import co.anitrend.data.jikan.author.mapper.JikanAuthorMapper
import co.anitrend.data.jikan.contract.JikanItem
import co.anitrend.data.jikan.licensor.mapper.JikanLicensorMapper
import co.anitrend.data.jikan.media.converters.JikanModelConverter
import co.anitrend.data.jikan.media.datasource.local.JikanLocalSource
import co.anitrend.data.jikan.media.entity.JikanEntity
import co.anitrend.data.jikan.media.model.anime.JikanMediaModel
import co.anitrend.data.jikan.model.JikanWrapper
import co.anitrend.data.jikan.producer.mapper.JikanProducerMapper
import co.anitrend.data.jikan.studio.mapper.JikanStudioMapper

internal class JikanMapper(
    private val authorMapper: JikanAuthorMapper.Embed,
    private val licensorMapper: JikanLicensorMapper.Embed,
    private val producerMapper: JikanProducerMapper.Embed,
    private val studioMapper: JikanStudioMapper.Embed,
    private val localSource: JikanLocalSource,
    private val converter: JikanModelConverter
) : DefaultMapper<JikanWrapper<JikanMediaModel>, JikanEntity>() {

    /**
     * Save [data] into your desired local source
     */
    override suspend fun persist(data: JikanEntity) {
        localSource.upsert(data)
        licensorMapper.persistEmbedded()
        producerMapper.persistEmbedded()
        studioMapper.persistEmbedded()
        authorMapper.persistEmbedded()

    }

    /**
     * Creates mapped objects and handles the database operations which may be required to map various objects,
     *
     * @param source the incoming data source type
     * @return mapped object that will be consumed by [onResponseDatabaseInsert]
     */
    override suspend fun onResponseMapFrom(source: JikanWrapper<JikanMediaModel>): JikanEntity {
        when (val data = source.data) {
            is JikanMediaModel.Anime -> {
                licensorMapper.onEmbedded(
                    data.licensors?.map {
                        JikanItem(
                            jikanId = data.malId,
                            model = it
                        )
                    }.orEmpty()
                )
                producerMapper.onEmbedded(
                    data.producers?.map {
                        JikanItem(
                            jikanId = data.malId,
                            model = it
                        )
                    }.orEmpty()
                )
                studioMapper.onEmbedded(
                    data.studios?.map {
                        JikanItem(
                            jikanId = data.malId,
                            model = it
                        )
                    }.orEmpty()
                )
            }
            is JikanMediaModel.Manga -> {
                authorMapper.onEmbedded(
                    data.authors?.map {
                        JikanItem(
                            jikanId = data.malId,
                            model = it
                        )
                    }.orEmpty()
                )
            }
        }
        return converter.convertFrom(source)
    }
}
