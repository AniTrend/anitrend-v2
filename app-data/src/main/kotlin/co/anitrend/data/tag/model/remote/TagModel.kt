/*
 * Copyright (C) 2020  AniTrend
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

package co.anitrend.data.tag.model.remote

import co.anitrend.data.tag.model.contract.ITagModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal sealed class TagModel : ITagModel {

    @Serializable
    internal data class Core(
        @SerialName("name") override val name: String,
        @SerialName("description") override val description: String? = null,
        @SerialName("category") override val category: String? = null,
        @SerialName("isGeneralSpoiler") override val isGeneralSpoiler: Boolean? = null,
        @SerialName("isAdult") override val isAdult: Boolean? = null,
        @SerialName("id") override val id: Long
    ) : TagModel()

    /**
     * Tag model for media
     *
     * @param rank The relevance ranking of the tag out of the 100 for this media
     * @param isMediaSpoiler If the tag is a spoiler for this media
     */
    @Serializable
    internal data class Extended(
        @SerialName("rank") val rank: Int? = null,
        @SerialName("isMediaSpoiler") val isMediaSpoiler: Boolean? = null,
        @SerialName("name") override val name: String,
        @SerialName("description") override val description: String? = null,
        @SerialName("category") override val category: String? = null,
        @SerialName("isGeneralSpoiler") override val isGeneralSpoiler: Boolean? = null,
        @SerialName("isAdult") override val isAdult: Boolean? = null,
        @SerialName("id") override val id: Long
    ) : TagModel()
}