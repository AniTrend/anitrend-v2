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

package co.anitrend.data.user.model.remote.statistics

import co.anitrend.data.media.model.MediaModelExtended
import co.anitrend.data.user.model.remote.statistics.contract.IStatistic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** [UserTagStatistic](https://anilist.github.io/ApiV2-GraphQL-Docs/usertagstatistic.doc.html)
 *
 * @param tag media tag
 */
@Serializable
internal data class UserTagStatistic(
    @SerialName("tag") val tag: MediaModelExtended.Tag?,
    @SerialName("chaptersRead") override val chaptersRead: Int,
    @SerialName("count") override val count: Int,
    @SerialName("meanScore") override val meanScore: Float,
    @SerialName("mediaIds") override val mediaIds: List<Int>,
    @SerialName("minutesWatched") override val minutesWatched: Int
) : IStatistic
