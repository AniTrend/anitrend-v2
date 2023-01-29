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

package co.anitrend.data.media.model.edge

import co.anitrend.data.airing.model.AiringScheduleModel
import co.anitrend.data.character.model.remote.CharacterModel
import co.anitrend.data.common.entity.IEntityEdge
import co.anitrend.data.core.common.Identity
import co.anitrend.data.media.model.MediaModel
import co.anitrend.data.staff.model.StaffModel
import co.anitrend.domain.character.enums.CharacterRole
import co.anitrend.domain.media.enums.MediaRelation
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal sealed class MediaEdge {

    /** [MediaEdge](https://anilist.github.io/ApiV2-GraphQL-Docs/mediaedge.doc.html)
     * Media connection edge
     */
    @Serializable
    data class Airing(
        @SerialName("node") override val node: AiringScheduleModel.Core?,
        @SerialName("id") override val id: Long
    ) : MediaEdge(), IEntityEdge<AiringScheduleModel>, Identity

    /** [MediaEdge](https://anilist.github.io/ApiV2-GraphQL-Docs/mediaedge.doc.html)
     * Media connection edge
     */
    @Serializable
    data class Recommendation(
        @SerialName("node") override val node: MediaModel.Core?
    ) : MediaEdge(), IEntityEdge<MediaModel>

    /** [MediaEdge](https://anilist.github.io/ApiV2-GraphQL-Docs/mediaedge.doc.html)
     * Media connection edge
     *
     * @param mediaRelation The type of relation to the parent model
     */
    @Serializable
    data class Relation(
        @SerialName("relationType") val mediaRelation: MediaRelation?,
        @SerialName("node") override val node: MediaModel.Core?,
        @SerialName("id") override val id: Long
    ) : MediaEdge(), IEntityEdge<MediaModel>, Identity

    /** [MediaEdge](https://anilist.github.io/ApiV2-GraphQL-Docs/mediaedge.doc.html)
     * Media connection edge
     *
     * @param isMainStudio If the studio is the main animation studio of the media (For Studio->MediaConnection field only)
     */
    @Serializable
    data class Studio(
        @SerialName("isMainStudio") val isMainStudio: Boolean,
        @SerialName("node") override val node: MediaModel.Core?,
        @SerialName("id") override val id: Long
    ) : MediaEdge(), IEntityEdge<MediaModel>, Identity

    /** [MediaEdge](https://anilist.github.io/ApiV2-GraphQL-Docs/mediaedge.doc.html)
     * Media connection edge
     *
     * @param favouriteOrder The order the media should be displayed from the users favourites
     */
    @Serializable
    data class Favourite(
        @SerialName("favouriteOrder") val favouriteOrder: Int?,
        @SerialName("node") override val node: MediaModel.Core?,
        @SerialName("id") override val id: Long
    ) : MediaEdge(), IEntityEdge<MediaModel>, Identity

    /** [MediaEdge](https://anilist.github.io/ApiV2-GraphQL-Docs/mediaedge.doc.html)
     * Media connection edge
     *
     * @param characterRole The characters role in the media
     * @param name Media specific role name
     * @param voiceActorRoles The voice actors of the character with role date
     */
    @Serializable
    data class Character(
        @SerialName("role") val characterRole: CharacterRole?,
        @SerialName("name") val name: String?,
        @SerialName("voiceActorRoles") val voiceActorRoles: List<StaffModel.ActorRole>?,
        @SerialName("node") override val node: CharacterModel.Core?,
        @SerialName("id") override val id: Long
    ) : MediaEdge(), IEntityEdge<CharacterModel>, Identity

    /** [MediaEdge](https://anilist.github.io/ApiV2-GraphQL-Docs/mediaedge.doc.html)
     * Media connection edge
     *
     * @param staffRole The role of the staff member in the production of the media
     * @param voiceActors The voice actors of the character
     */
    @Serializable
    data class Staff(
        @SerialName("role") val staffRole: String?,
        @SerialName("node") override val node: MediaModel.Core?,
        @SerialName("id") override val id: Long
    ) : MediaEdge(), IEntityEdge<MediaModel>, Identity
}