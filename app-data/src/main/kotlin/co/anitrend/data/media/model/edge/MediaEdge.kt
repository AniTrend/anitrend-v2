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

import co.anitrend.data.arch.common.entity.IEntityEdge
import co.anitrend.data.character.model.remote.CharacterModel
import co.anitrend.data.media.model.MediaModelCore
import co.anitrend.data.staff.model.remote.StaffModel
import co.anitrend.domain.character.enums.CharacterRole
import co.anitrend.domain.media.enums.MediaRelation
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** [MediaEdge](https://anilist.github.io/ApiV2-GraphQL-Docs/mediaedge.doc.html)
 * Media connection edge
 *
 * @property characterRole The characters role in the media
 * @property characters The characters in the media voiced by the parent actor
 * @property favouriteOrder The order the media should be displayed from the users favourites
 * @property isMainStudio If the studio is the main animation studio of the media (For Studio->MediaConnection field only)
 * @property relationType The type of relation to the parent model
 * @property staffRole The role of the staff member in the production of the media
 * @property voiceActors The voice actors of the character
 */
@Serializable
internal data class MediaEdge(
    @SerialName("characterRole") val characterRole: CharacterRole?,
    @SerialName("characters") val characters: List<CharacterModel>?,
    @SerialName("favouriteOrder") val favouriteOrder: Int?,
    @SerialName("isMainStudio") val isMainStudio: Boolean,
    @SerialName("relationType") val relationType: MediaRelation?,
    @SerialName("staffRole") val staffRole: String?,
    @SerialName("voiceActors") val voiceActors: List<StaffModel>?,
    @SerialName("id") override val id: Long,
    @SerialName("node") override val node: MediaModelCore?
) : IEntityEdge<MediaModelCore>