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
import co.anitrend.arch.data.transformer.ISupportTransformer
import co.anitrend.data.genre.entity.GenreEntity
import co.anitrend.data.genre.mapper.withEmoji
import co.anitrend.domain.genre.entity.Genre

internal class GenreEntityConverter(
    override val fromType: (GenreEntity) -> Genre = ::transform,
    override val toType: (Genre) -> GenreEntity = { throw NotImplementedError() }
) : SupportConverter<GenreEntity, Genre>() {
    private companion object : ISupportTransformer<GenreEntity, Genre> {
        override fun transform(source: GenreEntity) = Genre.Core(
            name = source.genre,
            decorated = withEmoji(
                source.genre
            ),
            id = source.id
        )
    }
}

internal class GenreModelConverter(
    override val fromType: (String) -> GenreEntity = ::transform,
    override val toType: (GenreEntity) -> String = { throw NotImplementedError() }
) : SupportConverter<String, GenreEntity>() {
    private companion object : ISupportTransformer<String, GenreEntity> {
        override fun transform(source: String) = GenreEntity(genre = source)
    }
}