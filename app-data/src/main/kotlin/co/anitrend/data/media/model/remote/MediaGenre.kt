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

package co.anitrend.data.media.model.remote

import co.anitrend.arch.data.mapper.contract.ISupportMapperHelper
import co.anitrend.data.genre.entity.GenreEntity
import co.anitrend.data.media.model.contract.IMediaGenre

/**
 * If AniList ever duplicates genres we're screwed! **R.I.P**
 */
internal data class MediaGenre(
    override val genre: String
) : IMediaGenre {

    companion object : ISupportMapperHelper<IMediaGenre, GenreEntity> {
        /**
         * Transforms the the [source] to the target type
         */
        override fun transform(source: IMediaGenre) =
            GenreEntity(
                genre = source.genre
            )
    }
}