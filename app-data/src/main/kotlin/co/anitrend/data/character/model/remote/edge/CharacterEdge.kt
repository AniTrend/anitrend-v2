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

import co.anitrend.data.arch.common.entity.IEntityEdge
import co.anitrend.data.character.model.remote.CharacterModel
import co.anitrend.data.media.model.MediaModel
import co.anitrend.data.staff.model.remote.StaffModel
import co.anitrend.domain.character.enums.CharacterRole
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** [CharacterEdge](https://anilist.github.io/ApiV2-GraphQL-Docs/characteredge.doc.html)
 * Character connection edge
 *
 * @param favouriteOrder The order the character should be displayed from the users favourites
 * @param media The media the character is in
 * @param role The characters role in the media
 * @param voiceActors The voice actors of the character
 */
@Serializable
internal data class CharacterEdge(
    @SerialName("favouriteOrder") val favouriteOrder: Int?,
    @SerialName("media") val media: List<MediaModel.Core>?,
    @SerialName("role") val role: CharacterRole?,
    @SerialName("voiceActors") val voiceActors: List<StaffModel.Core>?,
    @SerialName("node") override val node: CharacterModel.Core?,
    @SerialName("id") override val id: Long,
) : IEntityEdge<CharacterModel>