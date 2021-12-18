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

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.data.android.mapper.DefaultMapper
import co.anitrend.data.android.mapper.EmbedMapper
import co.anitrend.data.core.extensions.toHashId
import co.anitrend.data.genre.converters.GenreModelConverter
import co.anitrend.data.genre.datasource.local.GenreLocalSource
import co.anitrend.data.genre.datasource.local.connection.GenreConnectionLocalSource
import co.anitrend.data.genre.entity.GenreEntity
import co.anitrend.data.genre.entity.connection.GenreConnectionEntity
import co.anitrend.data.genre.model.GenreCollection
import co.anitrend.data.media.model.MediaModel

internal sealed class GenreMapper : DefaultMapper<GenreCollection, List<GenreEntity>>() {

    protected abstract val localSource: GenreLocalSource
    protected abstract val converter: GenreModelConverter

    class Core(
        override val localSource: GenreLocalSource,
        override val converter: GenreModelConverter
    ) : GenreMapper() {

        /**
         * Save [data] into your desired local source
         */
        override suspend fun persist(data: List<GenreEntity>) {
            localSource.upsert(data)
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

    class Embed(
        override val localSource: GenreConnectionLocalSource
    ) : EmbedMapper<Embed.Item, GenreConnectionEntity>() {

        override val converter = object : SupportConverter<Item, GenreConnectionEntity>() {
            /**
             * Function reference from converting from [M] to [E] which will
             * be called by [convertFrom]
             */
            override val fromType: (Item) -> GenreConnectionEntity = {
                GenreConnectionEntity(
                    mediaId = it.mediaId,
                    genreId = it.genreId
                )
            }

            /**
             * Function reference from converting from [E] to [M] which will
             * be called by [convertTo]
             */
            override val toType: (GenreConnectionEntity) -> Item
                get() = throw NotImplementedError()
        }

        data class Item(
            val mediaId: Long,
            val genreId: Long
        )

        companion object {

            fun asItem(source: MediaModel) =
                source.genres?.map { genre ->
                    Item(
                        mediaId = source.id,
                        genreId = genre.toHashId()
                    )
                }.orEmpty()

            fun asItem(source: List<MediaModel>) =
                source.flatMap { media ->
                    asItem(media)
                }
        }
    }
}