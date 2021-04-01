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

package co.anitrend.data.studio.model.edge

import co.anitrend.data.common.entity.IEntityEdge
import co.anitrend.data.studio.model.StudioModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal sealed class StudioEdge : IEntityEdge<StudioModel> {

    /** [StudioEdge](https://anilist.github.io/ApiV2-GraphQL-Docs/studioedge.doc.html)
     * Studio connection edge
     *
     * @param favouriteOrder The order the character should be displayed from the users favourites
     */
    @Serializable
    data class Favourite(
        @SerialName("favouriteOrder") val favouriteOrder: Int?,
        @SerialName("node") override val node: StudioModel?,
        @SerialName("id") override val id: Long
    ) : StudioEdge()

    /** [StudioEdge](https://anilist.github.io/ApiV2-GraphQL-Docs/studioedge.doc.html)
     * Studio connection edge
     *
     * @param isMain If the studio is the main animation studio of the anime
     */
    @Serializable
    data class Media(
        @SerialName("isMain") val isMain: Boolean,
        @SerialName("node") override val node: StudioModel?,
        @SerialName("id") override val id: Long
    ) : StudioEdge()
}