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

package co.anitrend.data.staff.model.remote.edge

import co.anitrend.data.arch.common.entity.IEntityEdge
import co.anitrend.data.staff.model.remote.StaffModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** [StaffEdge](https://anilist.github.io/ApiV2-GraphQL-Docs/staffedge.doc.html)
 * Staff connection edge
 *
 * @param favouriteOrder The order the staff should be displayed from the users favourites
 * @param role The role of the staff member in the production of the media
 */
@Serializable
internal data class StaffEdge(
    @SerialName("favouriteOrder") val favouriteOrder: Int?,
    @SerialName("role") val role: String?,
    @SerialName("id") override val id: Long,
    @SerialName("node") override val node: StaffModel.Core?
) : IEntityEdge<StaffModel>