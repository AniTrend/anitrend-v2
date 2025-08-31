/*
 * Copyright (C) 2025 AniTrend
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
package co.anitrend.data.edge.news.model.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class EdgeNewsConnectionModel(
    @SerialName("newsConnection") val connection: Connection,
) {
    @Serializable
    data class Connection(
        @SerialName("count") val count: Int,
        @SerialName("first") val first: String,
        @SerialName("last") val last: String,
        @SerialName("data") val data: List<News>,
    )

    @Serializable
    data class News(
        @SerialName("id") val id: String,
        @SerialName("title") val title: String,
        @SerialName("link") val link: String,
        @SerialName("image") val image: String? = null,
        @SerialName("author") val author: String? = null,
        @SerialName("publishedOn") val publishedOn: Long? = null,
        @SerialName("description") val description: String? = null,
        @SerialName("content") val content: String? = null,
    )
}
