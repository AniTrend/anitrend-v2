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
import co.anitrend.data.media.model.connection.MediaConnection
import co.anitrend.data.shared.model.SharedImage
import co.anitrend.data.shared.model.SharedName
import co.anitrend.data.staff.model.contract.IStaffModel
import co.anitrend.domain.staff.enums.StaffLanguage
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** [Staff](Notification](https://anilist.github.io/ApiV2-GraphQL-Docs/staff.doc.html)
 * Voice actors or production staff
 *
 * @param character Characters voiced by the actor
 * @param staffMedia Media where the staff member has a production role
 */
@Serializable
internal data class StaffModel(
    val character: CharacterConnection?,
    val staffMedia: MediaConnection?,
    @SerialName("description") override val description: String?,
    @SerialName("favourites") override val favourites: Int,
    @SerialName("image") override val image: SharedImage?,
    @SerialName("isFavourite") override val isFavourite: Boolean,
    @SerialName("language") override val language: StaffLanguage?,
    @SerialName("name") override val name: SharedName?,
    @SerialName("siteUrl") override val siteUrl: String?,
    @SerialName("updatedAt") override val updatedAt: Long?,
    @SerialName("id") override val id: Long
) : IStaffModel