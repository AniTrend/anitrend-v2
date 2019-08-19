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

package co.anitrend.data.model.mutation.favourite

import co.anitrend.data.model.response.contract.IGraphQuery

/** [UpdateFavouriteOrder mutation](https://anilist.github.io/ApiV2-GraphQL-Docs/mutation.doc.html)
 *
 * Update the order favourites are displayed in
 *
 * @param animeIds The id of the anime to un/favourite
 * @param mangaIds The id of the manga to un/favourite
 * @param characterIds The id of the character to un/favourite
 * @param staffIds The id of the staff to un/favourite
 * @param studioIds The id of the studio to un/favourite
 * @param animeOrder List of integers which the anime should be ordered by (Asc)
 * @param mangaOrder List of integers which the manga should be ordered by (Asc)
 * @param characterOrder List of integers which the character should be ordered by (Asc)
 * @param staffOrder List of integers which the staff should be ordered by (Asc)
 * @param studioOrder List of integers which the studio should be ordered by (Asc)
 */
data class UpdateFavouriteOrderMutation(
    val animeIds: List<Int>? = null,
    val mangaIds: List<Int>? = null,
    val characterIds: List<Int>? = null,
    val staffIds: List<Int>? = null,
    val studioIds: List<Int>? = null,
    val animeOrder: List<Int>? = null,
    val mangaOrder: List<Int>? = null,
    val characterOrder: List<Int>? = null,
    val staffOrder: List<Int>? = null,
    val studioOrder: List<Int>? = null
) : IGraphQuery {

    /**
     * A map serializer to build maps out of object so that we can consume them
     * using [io.github.wax911.library.model.request.QueryContainerBuilder].
     */
    override fun toMap() = mapOf(
        "animeIds" to animeIds,
        "mangaIds" to mangaIds,
        "characterIds" to characterIds,
        "staffIds" to staffIds,
        "studioIds" to studioIds,
        "animeOrder" to animeOrder,
        "mangaOrder" to mangaOrder,
        "characterOrder" to characterOrder,
        "staffOrder" to staffOrder,
        "studioOrder" to studioOrder
    )
}