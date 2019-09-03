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

package co.anitrend.data.model.core.studio.connection

import co.anitrend.domain.common.entity.IEntityConnection
import co.anitrend.data.model.core.studio.Studio
import co.anitrend.data.model.core.studio.edge.StudioEdge
import co.anitrend.domain.entities.common.PageInfo

/** [StudioConnection](https://anilist.github.io/ApiV2-GraphQL-Docs/studioconnection.doc.html)
 * Studio Connection
 */
data class StudioConnection(
    override val edges: List<Studio>?,
    override val nodes: List<StudioEdge>?,
    override val pageInfo: PageInfo?
) : IEntityConnection<Studio, StudioEdge>