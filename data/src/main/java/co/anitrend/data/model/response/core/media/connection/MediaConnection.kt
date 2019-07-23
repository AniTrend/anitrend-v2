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

package co.anitrend.data.model.response.core.media.connection

import co.anitrend.data.entity.contract.IEntityConnection
import co.anitrend.data.model.response.core.media.Media
import co.anitrend.data.model.response.core.media.edge.MediaEdge
import co.anitrend.data.model.response.meta.PageInfo

/** [MediaConnection](https://anilist.github.io/ApiV2-GraphQL-Docs/mediaconnection.doc.html)
 * Media connection
 */
data class MediaConnection(
    override val edges: List<MediaEdge>?,
    override val nodes: List<Media>?,
    override val pageInfo: PageInfo?
) : IEntityConnection<MediaEdge, Media>