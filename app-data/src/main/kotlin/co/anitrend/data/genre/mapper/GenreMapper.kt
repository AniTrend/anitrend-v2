/*
 * Copyright (C) 2019  AniTrend
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

package co.anitrend.data.genre.mapper

import co.anitrend.data.android.extensions.runInTransaction
import co.anitrend.data.android.mapper.DefaultMapper
import co.anitrend.data.genre.converters.GenreModelConverter
import co.anitrend.data.genre.datasource.local.GenreLocalSource
import co.anitrend.data.genre.entity.GenreEntity
import co.anitrend.data.genre.entity.connection.GenreConnectionEntity
import co.anitrend.data.genre.model.GenreCollection
import co.anitrend.data.media.model.MediaModel

internal sealed class GenreMapper : DefaultMapper<GenreCollection, List<GenreEntity>>() {

    protected abstract val localSource: GenreLocalSource
    protected abstract val converter: GenreModelConverter

    private var connections: List<GenreConnectionEntity>? = null

    suspend fun persistConnection() {
        connections?.also {
            localSource.upsertConnections(it)
        }
        connections = null
    }

    fun onConnection(source: List<MediaModel>) {
        connections = source.flatMap { media ->
            media.genres?.map {
                GenreConnectionEntity(
                    mediaId = media.id,
                    genre = it
                )
            }.orEmpty()
        }
    }

    class Core(
        override val localSource: GenreLocalSource,
        override val converter: GenreModelConverter
    ) : GenreMapper() {

        /**
         * Save [data] into your desired local source
         */
        override suspend fun persist(data: List<GenreEntity>) {
            runInTransaction {
                val ids = localSource.insert(data)
                if (ids.isEmpty())
                    localSource.update(data)
            }
        }

        /**
         * Creates mapped objects and handles the database operations which may be required to map various objects
         *
         * @param source the incoming data source type
         * @return Mapped object that will be consumed by [onResponseDatabaseInsert]
         */
        override suspend fun onResponseMapFrom(
            source: GenreCollection
        ) = converter.convertFrom(
            source.asGenreModels()
        )
    }
}