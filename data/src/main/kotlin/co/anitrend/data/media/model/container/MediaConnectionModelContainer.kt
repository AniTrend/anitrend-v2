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

import co.anitrend.data.media.model.connection.MediaConnection
import co.anitrend.data.recommendation.model.connection.RecommendationConnection
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal sealed class MediaConnectionModelContainer {
    @Serializable
    data class Relations(
        @SerialName("Media") val media: Media? = null,
    ) {
        @Serializable
        data class Media(
            @SerialName("relations") val relations: MediaConnection.Relation? = null,
        )
    }

    @Serializable
    data class Recommendations(
        @SerialName("Media") val media: Media? = null,
    ) {
        @Serializable
        data class Media(
            @SerialName("recommendations") val recommendations: RecommendationConnection? = null,
        )
    }
}
