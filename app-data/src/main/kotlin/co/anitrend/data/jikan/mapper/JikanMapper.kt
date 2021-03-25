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

package co.anitrend.data.jikan.mapper

import co.anitrend.data.arch.mapper.DefaultMapper
import co.anitrend.data.jikan.converters.*
import co.anitrend.data.jikan.datasource.local.JikanLocalSource
import co.anitrend.data.jikan.entity.JikanEntity
import co.anitrend.data.jikan.model.anime.JikanMediaModel

internal class JikanMapper(
    private val localSource: JikanLocalSource,
    private val converter: JikanModelConverter
) : DefaultMapper<JikanMediaModel, JikanEntity>() {

    private var authors: List<JikanEntity.AuthorEntity>? = null
    private var producers: List<JikanEntity.ProducerEntity>? = null
    private var licensors: List<JikanEntity.LicensorEntity>? = null
    private var studios: List<JikanEntity.StudioEntity>? = null

    private fun onConnectionModel(source: JikanMediaModel) {
        when (source) {
            is JikanMediaModel.Anime -> {
                producers = JikanProducerModelConverter(source.malId)
                        .convertFrom(source.licensors.orEmpty())
                licensors = JikanLicensorModelConverter(source.malId)
                    .convertFrom(source.producers.orEmpty())
                studios = JikanStudioModelConverter(source.malId)
                    .convertFrom(source.studios.orEmpty())
            }
            is JikanMediaModel.Manga -> {
                authors = JikanAuthorModelConverter(source.malId)
                    .convertFrom(source.authors.orEmpty())
            }
        }
    }

    /**
     * Save [data] into your desired local source
     */
    override suspend fun persist(data: JikanEntity) {
        localSource.upsertWithConnections(
            data,
            authors.orEmpty(),
            producers.orEmpty(),
            licensors.orEmpty(),
            studios.orEmpty()
        )
        authors = null
        producers = null
        licensors = null
        studios = null
    }

    /**
     * Creates mapped objects and handles the database operations which may be required to map various objects,
     *
     * @param source the incoming data source type
     * @return mapped object that will be consumed by [onResponseDatabaseInsert]
     */
    override suspend fun onResponseMapFrom(source: JikanMediaModel): JikanEntity {
        onConnectionModel(source)
        return converter.convertFrom(source)
    }
}