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

package co.anitrend.data.media.model.connection

import co.anitrend.data.common.entity.IEntityConnection
import co.anitrend.data.common.model.paging.info.PageInfo
import co.anitrend.data.media.model.MediaModel
import co.anitrend.data.media.model.edge.MediaEdge
import co.anitrend.data.review.model.remote.ReviewModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** [MediaConnection](https://anilist.github.io/ApiV2-GraphQL-Docs/mediaconnection.doc.html)
 * Media connection
 */
@Serializable
internal sealed class MediaConnection : IEntityConnection {

    @Serializable
    data class Airing(
        @SerialName("edges") override val edges: List<MediaEdge.Airing>?,
        @SerialName("pageInfo") override val pageInfo: PageInfo?
    ) : MediaConnection(), IEntityConnection.IEdge<MediaEdge>

    @Serializable
    data class Review(
        @SerialName("nodes") override val nodes: List<ReviewModel.Core>?,
        @SerialName("pageInfo") override val pageInfo: PageInfo?
    ) : MediaConnection(), IEntityConnection.INode<ReviewModel>

    @Serializable
    data class Recommendation(
        @SerialName("edges") override val edges: List<MediaEdge.Recommendation>?,
        @SerialName("pageInfo") override val pageInfo: PageInfo?
    ) : MediaConnection(), IEntityConnection.IEdge<MediaEdge>

    @Serializable
    data class Relation(
        @SerialName("edges") override val edges: List<MediaEdge.Relation>?,
        @SerialName("pageInfo") override val pageInfo: PageInfo?
    ) : MediaConnection(), IEntityConnection.IEdge<MediaEdge>

    @Serializable
    data class Studio(
        @SerialName("edges") override val edges: List<MediaEdge.Studio>?,
        @SerialName("pageInfo") override val pageInfo: PageInfo?
    ) : MediaConnection(), IEntityConnection.IEdge<MediaEdge>

    @Serializable
    data class Favourite(
        @SerialName("edges") override val edges: List<MediaEdge.Favourite>?,
        @SerialName("pageInfo") override val pageInfo: PageInfo?
    ) : MediaConnection(), IEntityConnection.IEdge<MediaEdge>

    @Serializable
    data class Character(
        @SerialName("edges") override val edges: List<MediaEdge.Character>?,
        @SerialName("pageInfo") override val pageInfo: PageInfo?
    ) : MediaConnection(), IEntityConnection.IEdge<MediaEdge>

    @Serializable
    data class Staff(
        @SerialName("edges") override val edges: List<MediaEdge.Staff>?,
        @SerialName("pageInfo") override val pageInfo: PageInfo?
    ) : MediaConnection(), IEntityConnection.IEdge<MediaEdge>
}