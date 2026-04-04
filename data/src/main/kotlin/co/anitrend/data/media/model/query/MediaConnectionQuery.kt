/*
 * Copyright (C) 2026 AniTrend
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
package co.anitrend.data.media.model.query

import co.anitrend.data.common.model.graph.IGraphPayload
import co.anitrend.domain.media.model.MediaParam

internal sealed class MediaConnectionQuery : IGraphPayload {
    data class Relations(
        val param: MediaParam.Relations,
    ) : MediaConnectionQuery() {
        override fun toMap() =
            mapOf(
                "id" to param.id,
            )
    }

    data class Recommendations(
        val param: MediaParam.Recommendations,
    ) : MediaConnectionQuery() {
        override fun toMap() =
            mapOf(
                "page" to 1,
                "perPage" to param.perPage,
                "id" to param.id,
                "sort" to param.sort,
            )
    }
}
