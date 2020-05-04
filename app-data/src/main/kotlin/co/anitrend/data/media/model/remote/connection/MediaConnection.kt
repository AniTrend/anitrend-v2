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

package co.anitrend.data.media.model.remote.connection

import co.anitrend.data.arch.common.entity.IEntityConnection
import co.anitrend.data.media.model.remote.Media
import co.anitrend.data.media.model.remote.edge.MediaEdge
import co.anitrend.domain.common.model.PageInfo

/** [MediaConnection](https://anilist.github.io/ApiV2-GraphQL-Docs/mediaconnection.doc.html)
 * Media connection
 */
internal data class MediaConnection(
    override val edges: List<MediaEdge>?,
    override val nodes: List<Media>?,
    override val pageInfo: PageInfo?
) : IEntityConnection<MediaEdge, Media>