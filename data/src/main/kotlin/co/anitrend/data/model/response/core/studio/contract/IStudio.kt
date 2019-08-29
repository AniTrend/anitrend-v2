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

package co.anitrend.data.model.response.core.studio.contract

import co.anitrend.data.entity.contract.IEntity

/** [Studio](https://anilist.github.io/ApiV2-GraphQL-Docs/studio.doc.html)
 * Studio contract, without connections
 *
 * @property favourites The amount of user's who have favourite the studio
 * @property id The id of the studio
 * @property isAnimationStudio If the studio is an animation studio or a different kind of company
 * @property isFavourite If the studio is marked as favourite by the currently authenticated user
 * @property name The name of the studio
 * @property siteUrl The url for the studio page on the AniList website
*/
interface IStudio : IEntity {
    val favourites: Int?
    val isAnimationStudio: Boolean
    val isFavourite: Boolean
    val name: String
    val siteUrl: String?
}