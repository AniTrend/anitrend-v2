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

package co.anitrend.data.studio.model.remote.connection

import co.anitrend.data.arch.common.entity.IEntityConnection
import co.anitrend.data.arch.common.model.paging.info.PageInfo
import co.anitrend.data.studio.model.remote.StudioModel
import co.anitrend.data.studio.model.remote.edge.StudioEdge
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** [StudioConnection](https://anilist.github.io/ApiV2-GraphQL-Docs/studioconnection.doc.html)
 * Studio Connection
 */
@Serializable
internal data class StudioConnection(
    @SerialName("edges") override val edges: List<StudioEdge>?,
    @SerialName("nodes") override val nodes: List<StudioModel>?,
    @SerialName("pageInfo") override val pageInfo: PageInfo?
) : IEntityConnection<StudioEdge, StudioModel>