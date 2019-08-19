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

/** [ToggleFavourite mutation](https://anilist.github.io/ApiV2-GraphQL-Docs/mutation.doc.html)
 *
 * Favourite or un/favourite an anime, manga, character, staff member, or studio
 *
 * @param animeId The id of the anime to un/favourite
 * @param mangaId The id of the manga to un/favourite
 * @param characterId The id of the character to un/favourite
 * @param staffId The id of the staff to un/favourite
 * @param studioId The id of the studio to un/favourite
 */
data class ToggleFavouriteMutation(
    val animeId: Int? = null,
    val mangaId: Int? = null,
    val characterId: Int? = null,
    val staffId: Int? = null,
    val studioId: Int? = null
) : IGraphQuery {

    /**
     * A map serializer to build maps out of object so that we can consume them
     * using [io.github.wax911.library.model.request.QueryContainerBuilder].
     */
    override fun toMap() = mapOf(
        "animeId" to animeId,
        "mangaId" to mangaId,
        "characterId" to characterId,
        "staffId" to staffId,
        "studioId" to studioId
    )
}