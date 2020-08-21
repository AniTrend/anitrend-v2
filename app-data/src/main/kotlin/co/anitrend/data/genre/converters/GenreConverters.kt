/*
 * Copyright (C) 2020  AniTrend
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

package co.anitrend.data.genre.converters

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.arch.data.mapper.contract.ISupportMapperHelper
import co.anitrend.data.genre.entity.GenreEntity
import co.anitrend.domain.genre.entity.Genre

internal class GenreEntityConverter(
    override val fromType: (GenreEntity) -> Genre = { from().transform(it) },
    override val toType: (Genre) -> GenreEntity = { to().transform(it) }
) : SupportConverter<GenreEntity, Genre>() {

    companion object {
        private fun from() =
            object : ISupportMapperHelper<GenreEntity, Genre> {
                override fun transform(source: GenreEntity) = Genre(
                    name = source.genre
                )
            }

        private fun to() =
            object : ISupportMapperHelper<Genre, GenreEntity> {
                override fun transform(source: Genre) = GenreEntity(
                    genre = source.name
                )
            }
    }
}

internal class GenreModelConverter(
    override val fromType: (String) -> GenreEntity = { from().transform(it) },
    override val toType: (GenreEntity) -> String = { to().transform(it) }
) : SupportConverter<String, GenreEntity>() {

    companion object {
        private fun from() =
            object : ISupportMapperHelper<String, GenreEntity> {
                override fun transform(source: String): GenreEntity = GenreEntity(
                    genre = source
                )
            }

        private fun to() =
            object : ISupportMapperHelper<GenreEntity, String> {
                override fun transform(source: GenreEntity): String = source.genre
            }
    }
}