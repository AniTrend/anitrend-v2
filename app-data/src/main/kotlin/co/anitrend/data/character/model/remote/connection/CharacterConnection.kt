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

package co.anitrend.data.character.model.remote.connection

import co.anitrend.data.common.entity.IEntityConnection
import co.anitrend.data.common.model.paging.info.PageInfo
import co.anitrend.data.character.model.remote.CharacterModel
import co.anitrend.data.character.model.remote.edge.CharacterEdge
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** [CharacterConnection](https://anilist.github.io/ApiV2-GraphQL-Docs/characterconnection.doc.html)
 * Character Connection
 */
@Serializable
internal sealed class CharacterConnection : IEntityConnection,
    IEntityConnection.IEdge<CharacterEdge> , IEntityConnection.INode<CharacterModel> {

    @Serializable
    data class Favourite(
        @SerialName("edges") override val edges: List<CharacterEdge.Favourite>?,
        @SerialName("nodes") override val nodes: List<CharacterModel.Core>?,
        @SerialName("pageInfo") override val pageInfo: PageInfo?
    ) : CharacterConnection()

    @Serializable
    data class Media(
        @SerialName("edges") override val edges: List<CharacterEdge.Media>?,
        @SerialName("nodes") override val nodes: List<CharacterModel.Core>?,
        @SerialName("pageInfo") override val pageInfo: PageInfo?
    ) : CharacterConnection()

    @Serializable
    data class Staff(
        @SerialName("edges") override val edges: List<CharacterEdge.Staff>?,
        @SerialName("nodes") override val nodes: List<CharacterModel.Core>?,
        @SerialName("pageInfo") override val pageInfo: PageInfo?
    ) : CharacterConnection()
}