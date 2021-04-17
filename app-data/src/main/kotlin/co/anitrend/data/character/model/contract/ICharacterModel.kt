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

package co.anitrend.data.character.model.contract

import co.anitrend.data.core.common.Identity
import co.anitrend.data.shared.model.SharedImageModel
import co.anitrend.data.shared.model.SharedNameModel

/** [Character](https://anilist.github.io/ApiV2-GraphQL-Docs/character.doc.html)
 * Character contract without any connections
 *
 * @property description A general description of the character
 * @property favourites The amount of user's who have added the character to their favourites
 * @property id The id of the character
 * @property image Character images
 * @property isFavourite If the character is marked as favourite by the currently authenticated user
 * @property isFavouriteBlocked If the character is blocked from being added to favourites
 * @property name The names of the character
 * @property siteUrl The url for the character page on the AniList website
 */
internal interface ICharacterModel : Identity {
    val description: String?
    val favourites: Int?
    val image: SharedImageModel?
    val isFavourite: Boolean
    val isFavouriteBlocked: Boolean
    val name: SharedNameModel?
    val siteUrl: String?
}