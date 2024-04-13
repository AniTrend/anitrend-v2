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

package co.anitrend.domain.favourite.model

sealed class FavouriteInput {

    /** [ToggleFavourite mutation](https://anilist.github.io/ApiV2-GraphQL-Docs/mutation.doc.html)
     *
     * Favourite or un/favourite an anime, manga, character, staff member, or studio
     *
     * @param animeId The id of the anime to un/favourite
     */
    data class ToggleAnime(
        val animeId: Long? = null,
        val animeIds: List<Long>? = null
    ) : FavouriteInput()

    /** [ToggleFavourite mutation](https://anilist.github.io/ApiV2-GraphQL-Docs/mutation.doc.html)
     *
     * Favourite or un/favourite an anime, manga, character, staff member, or studio
     *
     * @param mangaId The id of the manga to un/favourite
     */
    data class ToggleManga(
        val mangaId: Long? = null,
        val mangaIds: List<Long>? = null
    ) : FavouriteInput()

    /** [ToggleFavourite mutation](https://anilist.github.io/ApiV2-GraphQL-Docs/mutation.doc.html)
     *
     * Favourite or un/favourite an anime, manga, character, staff member, or studio
     *
     * @param characterId The id of the character to un/favourite
     */
    data class ToggleCharacter(
        val characterId: Long? = null,
        val characterIds: List<Long>? = null
    ) : FavouriteInput()

    /** [ToggleFavourite mutation](https://anilist.github.io/ApiV2-GraphQL-Docs/mutation.doc.html)
     *
     * Favourite or un/favourite an anime, manga, character, staff member, or studio
     *
     * @param staffId The id of the staff to un/favourite
     */
    data class ToggleStaff(
        val staffId: Long? = null,
        val staffIds: List<Long>? = null
    ) : FavouriteInput()

    /** [ToggleFavourite mutation](https://anilist.github.io/ApiV2-GraphQL-Docs/mutation.doc.html)
     *
     * Favourite or un/favourite an anime, manga, character, staff member, or studio
     *
     * @param studioId The id of the studio to un/favourite
     */
    data class ToggleStudio(
        val studioId: Long? = null,
        val studioIds: List<Long>? = null
    ) : FavouriteInput()

    /** [UpdateFavouriteOrder mutation](https://anilist.github.io/ApiV2-GraphQL-Docs/mutation.doc.html)
     *
     * Update the order favourites are displayed in
     *
     * @param animeOrder List of integers which the anime should be ordered by (Asc)
     */
    data class UpdateOrderAnime(
        val animeOrder: List<Long>? = null
    ) : FavouriteInput()

    /** [UpdateFavouriteOrder mutation](https://anilist.github.io/ApiV2-GraphQL-Docs/mutation.doc.html)
     *
     * Update the order favourites are displayed in
     *
     * @param mangaOrder List of integers which the manga should be ordered by (Asc)
     */
    data class UpdateOrderManga(
        val mangaOrder: List<Long>? = null
    ) : FavouriteInput()

    /** [UpdateFavouriteOrder mutation](https://anilist.github.io/ApiV2-GraphQL-Docs/mutation.doc.html)
     *
     * Update the order favourites are displayed in
     *
     * @param characterOrder List of integers which the character should be ordered by (Asc)
     */
    data class UpdateOrderCharacter(
        val characterOrder: List<Long>? = null
    ) : FavouriteInput()

    /** [UpdateFavouriteOrder mutation](https://anilist.github.io/ApiV2-GraphQL-Docs/mutation.doc.html)
     *
     * Update the order favourites are displayed in
     *
     * @param staffOrder List of integers which the staff should be ordered by (Asc)
     */
    data class UpdateOrderStaff(
        val staffOrder: List<Long>? = null
    ) : FavouriteInput()

    /** [UpdateFavouriteOrder mutation](https://anilist.github.io/ApiV2-GraphQL-Docs/mutation.doc.html)
     *
     * Update the order favourites are displayed in
     *
     * @param studioOrder List of integers which the studio should be ordered by (Asc)
     */
    data class UpdateOrderStudio(
        val studioOrder: List<Long>? = null
    ) : FavouriteInput()
}
