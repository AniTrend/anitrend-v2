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
package co.anitrend.data.media.model.container

import co.anitrend.data.media.model.container.MediaSidecarModelContainer.Stats.Media.MediaStats
import co.anitrend.data.studio.model.connection.StudioConnection
import co.anitrend.domain.medialist.enums.MediaListStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal sealed class MediaSidecarModelContainer {
    @Serializable
    data class Studios(
        @SerialName("Media") val media: Media? = null,
    ) {
        @Serializable
        data class Media(
            @SerialName("studios") val studios: StudioConnection.Media? = null,
        )
    }

    @Serializable
    data class Stats(
        @SerialName("Media") val media: Media? = null,
    ) {
        @Serializable
        data class Media(
            @SerialName("stats") val stats: MediaStats? = null,
        ) {
            @Serializable
            data class MediaStats(
                @SerialName("scoreDistribution") val scoreDistribution: List<ScoreDistribution>? = null,
                @SerialName("statusDistribution") val statusDistribution: List<StatusDistribution>? = null,
            ) {
                @Serializable
                data class ScoreDistribution(
                    @SerialName("amount") val amount: Int? = null,
                    @SerialName("score") val score: Int? = null,
                )

                @Serializable
                data class StatusDistribution(
                    @SerialName("amount") val amount: Int? = null,
                    @SerialName("status") val status: MediaListStatus? = null,
                )
            }
        }
    }
}
