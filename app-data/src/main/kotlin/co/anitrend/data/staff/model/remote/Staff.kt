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

package co.anitrend.data.staff.model.remote

import co.anitrend.data.character.model.remote.connection.CharacterConnection
import co.anitrend.data.media.model.remote.connection.MediaConnection
import co.anitrend.data.staff.model.contract.IStaff
import co.anitrend.domain.common.model.CoverImage
import co.anitrend.domain.common.model.CoverName
import co.anitrend.domain.staff.enums.StaffLanguage

/** [Staff](Notification](https://anilist.github.io/ApiV2-GraphQL-Docs/staff.doc.html)
 * Voice actors or production staff
 *
 * @param character Characters voiced by the actor
 * @param staffMedia Media where the staff member has a production role
 */
data class Staff(
    val character: CharacterConnection?,
    val staffMedia: MediaConnection?,
    override val id: Long,
    override val description: String?,
    override val favourites: Int,
    override val image: CoverImage?,
    override val isFavourite: Boolean,
    override val language: StaffLanguage?,
    override val name: CoverName?,
    override val siteUrl: String?,
    override val updatedAt: Long?
) : IStaff