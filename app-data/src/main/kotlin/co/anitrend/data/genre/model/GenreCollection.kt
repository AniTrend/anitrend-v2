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

package co.anitrend.data.genre.model

import co.anitrend.data.core.extensions.toHashId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class GenreCollection(
    @SerialName("GenreCollection") val genreCollection: List<String>
) {

    fun asGenreModels() =
        genreCollection
            .sortedBy { it }
            .map { text ->
                GenreModel(
                    id = text.toHashId(),
                    genre = text
                )
            }

    data class GenreModel(
        val id: Long,
        val genre: String
    )
}