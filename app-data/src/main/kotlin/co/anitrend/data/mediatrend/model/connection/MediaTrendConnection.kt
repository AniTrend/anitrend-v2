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

package co.anitrend.data.mediatrend.model.connection

import co.anitrend.data.arch.common.entity.IEntityConnection
import co.anitrend.data.arch.common.model.paging.info.PageInfo
import co.anitrend.data.mediatrend.model.MediaTrendModel
import co.anitrend.data.mediatrend.model.edge.MediaTrendEdge

/** [MediaTrendConnection](https://anilist.github.io/ApiV2-GraphQL-Docs/mediatrendconnection.doc.html)
 * Media trend connection
 */
internal data class MediaTrendConnection(
    override val edges: List<MediaTrendEdge>?,
    override val nodes: List<MediaTrendModel.Core>?,
    override val pageInfo: PageInfo?
) : IEntityConnection<MediaTrendEdge, MediaTrendModel>