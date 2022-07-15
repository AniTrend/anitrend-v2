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

package co.anitrend.data.character.model.remote.edge

import co.anitrend.data.common.entity.IEntityEdge
import co.anitrend.data.core.common.Identity
import co.anitrend.data.media.model.MediaModel
import co.anitrend.data.staff.model.StaffModel
import co.anitrend.domain.character.enums.CharacterRole
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal sealed class CharacterEdge {

    /** [CharacterEdge](https://anilist.github.io/ApiV2-GraphQL-Docs/characteredge.doc.html)
     * Character connection edge
     *
     * @param favouriteOrder The order the character should be displayed from the users favourites
     */
    @Serializable
    data class Favourite(
        @SerialName("favouriteOrder") val favouriteOrder: Int? = null
    ) : CharacterEdge()

    /** [CharacterEdge](https://anilist.github.io/ApiV2-GraphQL-Docs/characteredge.doc.html)
     * Character connection edge
     *
     * @param characterRole The characters role in the media
     * @param media The media the character is in
     */
    @Serializable
    data class Media(
        @SerialName("characterRole") val characterRole: CharacterRole? = null,
        @SerialName("media") val media: List<MediaModel.Core>? = null,
        @SerialName("id") override val id: Long
    ) : CharacterEdge(), Identity

    /** [CharacterEdge](https://anilist.github.io/ApiV2-GraphQL-Docs/characteredge.doc.html)
     * Character connection edge
     *
     * @param voiceActorRoles The voice actors of the character
     */
    @Serializable
    data class Staff(
        @SerialName("characterRole") val characterRole: CharacterRole? = null,
        @SerialName("voiceActorRoles") val voiceActorRoles: List<StaffModel.ActorRole>? = null,
        @SerialName("node") override val node: MediaModel.Core? = null,
        @SerialName("id") override val id: Long
    ) : CharacterEdge(), IEntityEdge<MediaModel>, Identity
}