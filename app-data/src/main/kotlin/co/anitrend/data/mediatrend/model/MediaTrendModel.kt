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

package co.anitrend.data.mediatrend.model

import co.anitrend.data.media.model.MediaModel
import co.anitrend.data.mediatrend.model.contract.IMediaTrendModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** [MediaTrend](https://anilist.github.io/ApiV2-GraphQL-Docs/mediatrend.doc.html)
 * Daily media statistics
 */
@Serializable
internal sealed class MediaTrendModel : IMediaTrendModel {

    @Serializable
    internal data class Core(
        @SerialName("averageScore") override val averageScore: Int? = null,
        @SerialName("date") override val date: Long,
        @SerialName("episode") override val episode: Int? = null,
        @SerialName("inProgress") override val inProgress: Int? = null,
        @SerialName("mediaId") override val mediaId: Long,
        @SerialName("popularity") override val popularity: Int? = null,
        @SerialName("releasing") override val releasing: Boolean,
        @SerialName("trending") override val trending: Int
    ) : MediaTrendModel()

    @Serializable
    internal data class Extended(
        @SerialName("media") val media: MediaModel.Core,
        @SerialName("averageScore") override val averageScore: Int? = null,
        @SerialName("date") override val date: Long,
        @SerialName("episode") override val episode: Int? = null,
        @SerialName("inProgress") override val inProgress: Int? = null,
        @SerialName("mediaId") override val mediaId: Long,
        @SerialName("popularity") override val popularity: Int? = null,
        @SerialName("releasing") override val releasing: Boolean,
        @SerialName("trending") override val trending: Int
    ) : MediaTrendModel()
}