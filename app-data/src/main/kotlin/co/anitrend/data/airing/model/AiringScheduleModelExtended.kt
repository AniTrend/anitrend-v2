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

package co.anitrend.data.airing.model

import co.anitrend.data.media.model.MediaModelExtended
import co.anitrend.data.shared.common.Identity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** [AiringSchedule](https://anilist.github.io/ApiV2-GraphQL-Docs/airingschedule.doc.html)
 * Media Airing Schedule
 *
 * @param media The associate media of the airing episode
 */
@Serializable
internal data class AiringScheduleModelExtended(
    @SerialName("media") val media: MediaModelExtended?,
    @SerialName("airingAt") val airingAt: Long,
    @SerialName("episode") val episode: Int,
    @SerialName("mediaId") val mediaId: Long,
    @SerialName("timeUntilAiring") val timeUntilAiring: Long,
    @SerialName("id") override val id: Long
) : Identity