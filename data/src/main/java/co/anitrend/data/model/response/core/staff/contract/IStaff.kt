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

package co.anitrend.data.model.response.core.staff.contract

import co.anitrend.data.entity.contract.IEntity
import co.anitrend.data.entity.contract.IEntityImage
import co.anitrend.data.entity.contract.IEntityName
import co.anitrend.data.usecase.staff.attributes.StaffLanguage

/** [Staff](Notification](https://anilist.github.io/ApiV2-GraphQL-Docs/staff.doc.html)
 * Voice actors or production staff
 *
 * @property description A general description of the staff member
 * @property favourites The amount of user's who have favourited the staff member
 * @property image The staff images
 * @property isFavourite If the staff member is marked as favourite by the currently authenticated user
 * @property language The primary language of the staff member
 * @property name The names of the staff member
 * @property siteUrl The url for the staff page on the AniList website
 * @property updatedAt When the staff's data was last updated
 */
interface IStaff : IEntity {
    val description: String?
    val favourites: Int
    val image: IEntityImage?
    val isFavourite: Boolean
    val language: StaffLanguage?
    val name: IEntityName?
    val siteUrl: String?
    val updatedAt: Long?
}