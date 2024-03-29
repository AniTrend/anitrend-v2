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

package co.anitrend.data.mediatrend.model.edge

import co.anitrend.data.common.entity.IEntityEdge
import co.anitrend.data.core.common.Identity
import co.anitrend.data.mediatrend.model.MediaTrendModel

/** [MediaTrendEdge](https://anilist.github.io/ApiV2-GraphQL-Docs/mediatrendedge.doc.html)
 * Media trend connection edge
 *
 * **N.B.** The schema doesn't use the [id] field for MediaTrendEdge
 */
internal data class MediaTrendEdge(
    override val id: Long,
    override val node: MediaTrendModel.Core?
) : IEntityEdge<MediaTrendModel>, Identity