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

package co.anitrend.domain.favourite.model.mutation

import co.anitrend.domain.common.graph.IGraphPayload

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
    val animeIds: List<Long>? = null,
    val mangaIds: List<Long>? = null,
    val characterIds: List<Long>? = null,
    val staffIds: List<Long>? = null,
    val studioIds: List<Long>? = null,
    val animeOrder: List<Long>? = null,
    val mangaOrder: List<Long>? = null,
    val characterOrder: List<Long>? = null,
    val staffOrder: List<Long>? = null,
    val studioOrder: List<Long>? = null
) : IGraphPayload {

    /**
     * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
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