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

import co.anitrend.data.airing.model.contract.IAiringScheduleModel
import co.anitrend.data.media.model.MediaModel
import co.anitrend.data.shared.common.Identity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal sealed class AiringScheduleModel : IAiringScheduleModel {

    @Serializable
    internal data class Core(
        @SerialName("airingAt") override val airingAt: Long,
        @SerialName("episode") override val episode: Int,
        @SerialName("mediaId") override val mediaId: Long,
        @SerialName("timeUntilAiring") override val timeUntilAiring: Long,
        @SerialName("id") override val id: Long
    ) : AiringScheduleModel()

    @Serializable
    internal data class Extended(
        @SerialName("media") val media: MediaModel.Core?,
        @SerialName("airingAt") override val airingAt: Long,
        @SerialName("episode") override val episode: Int,
        @SerialName("mediaId") override val mediaId: Long,
        @SerialName("timeUntilAiring") override val timeUntilAiring: Long,
        @SerialName("id") override val id: Long
    ) : AiringScheduleModel()
}