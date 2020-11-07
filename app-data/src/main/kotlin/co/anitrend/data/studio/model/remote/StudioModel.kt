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

package co.anitrend.data.studio.model.remote

import co.anitrend.data.media.model.connection.MediaConnection
import co.anitrend.data.studio.model.contract.IStudioModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** [Studio](https://anilist.github.io/ApiV2-GraphQL-Docs/studio.doc.html)
 * Animation or production company
 */
@Serializable
internal sealed class StudioModel : IStudioModel {

    @Serializable
    internal data class Core(
        @SerialName("favourites") override val favourites: Int?,
        @SerialName("isAnimationStudio") override val isAnimationStudio: Boolean,
        @SerialName("isFavourite") override val isFavourite: Boolean,
        @SerialName("name") override val name: String,
        @SerialName("siteUrl") override val siteUrl: String?,
        @SerialName("id") override val id: Long
    ) : StudioModel()

    /**
     * @param mediaConnection
     */
    @Serializable
    internal data class Extended(
        @SerialName("media") val mediaConnection: MediaConnection?,
        @SerialName("favourites") override val favourites: Int?,
        @SerialName("isAnimationStudio") override val isAnimationStudio: Boolean,
        @SerialName("isFavourite") override val isFavourite: Boolean,
        @SerialName("name") override val name: String,
        @SerialName("siteUrl") override val siteUrl: String?,
        @SerialName("id") override val id: Long
    ) : StudioModel()
}