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

package co.anitrend.data.staff.model.connection

import co.anitrend.data.common.entity.IEntityConnection
import co.anitrend.data.common.model.paging.info.PageInfo
import co.anitrend.data.media.model.MediaModel
import co.anitrend.data.staff.model.StaffModel
import co.anitrend.data.staff.model.edge.StaffEdge
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** [StaffEdge](https://anilist.github.io/ApiV2-GraphQL-Docs/staffedge.doc.html)
 * Staff Connection
 *
 */
@Serializable
internal sealed class StaffConnection : IEntityConnection {

    @Serializable
    data class Favourite(
        @SerialName("edges") override val edges: List<StaffEdge.Favourite>?,
        @SerialName("pageInfo") override val pageInfo: PageInfo?
    ) : StaffConnection(), IEntityConnection.IEdge<StaffEdge>

    @Serializable
    data class MediaWithRole(
        @SerialName("edges") override val edges: List<StaffEdge.MediaWithRole>?,
        @SerialName("pageInfo") override val pageInfo: PageInfo?
    ) : StaffConnection(), IEntityConnection.IEdge<StaffEdge>

    @Serializable
    data class Media(
        @SerialName("nodes") override val nodes: List<MediaModel.Core>?,
        @SerialName("pageInfo") override val pageInfo: PageInfo?
    ) : StaffConnection(), IEntityConnection.INode<MediaModel>
}