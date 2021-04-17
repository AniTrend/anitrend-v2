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

package co.anitrend.data.staff.model.contract

import co.anitrend.data.core.common.Identity
import co.anitrend.data.shared.model.SharedImageModel
import co.anitrend.data.shared.model.SharedNameModel
import co.anitrend.domain.staff.enums.StaffLanguage

/** [Staff](https://anilist.github.io/ApiV2-GraphQL-Docs/staff.doc.html)
 * Voice actors or production staff
 *
 * @property description A general description of the staff member
 * @property favourites The amount of user's who have favourited the staff member
 * @property image The staff images
 * @property isFavourite If the staff member is marked as favourite by the currently authenticated user
 * @property isFavouriteBlocked If the staff member is blocked from being added to favourites
 * @property language The primary language of the staff member
 * @property name The names of the staff member
 * @property siteUrl The url for the staff page on the AniList website
 */
internal interface IStaffModel : Identity {
    val description: String?
    val favourites: Int
    val image: SharedImageModel?
    val isFavourite: Boolean
    val isFavouriteBlocked: Boolean
    val language: StaffLanguage?
    val name: SharedNameModel?
    val siteUrl: String?
}