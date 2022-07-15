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

package co.anitrend.data.medialist.model

import co.anitrend.data.common.model.date.FuzzyDateModel
import co.anitrend.data.media.model.MediaModel
import co.anitrend.data.medialist.model.contract.IMediaListModel
import co.anitrend.data.user.model.UserModel
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.medialist.enums.MediaListStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal sealed class MediaListModel : IMediaListModel {

    @Serializable
    internal data class CustomList(
        @SerialName("name") override val name: String,
        @SerialName("enabled") override val enabled: Boolean
    ) : IMediaListModel.ICustomList

    @Serializable
    internal data class Category(
        @SerialName("type") val type: MediaType
    )

    @Serializable
    internal data class Core(
        @SerialName("category") val category: Category,
        @SerialName("advancedScores") override val advancedScores: Map<String, Float>? = null,
        @SerialName("customLists") override val customLists: List<CustomList>? = null,
        @SerialName("completedAt") override val completedAt: FuzzyDateModel? = null,
        @SerialName("createdAt") override val createdAt: Long,
        @SerialName("hiddenFromStatusLists") override val hiddenFromStatusLists: Boolean? = null,
        @SerialName("mediaId") override val mediaId: Long,
        @SerialName("notes") override val notes: String? = null,
        @SerialName("priority") override val priority: Int? = null,
        @SerialName("private") override val private: Boolean? = null,
        @SerialName("progress") override val progress: Int? = null,
        @SerialName("progressVolumes") override val progressVolumes: Int? = null,
        @SerialName("repeat") override val repeat: Int? = null,
        @SerialName("score") override val score: Float? = null,
        @SerialName("startedAt") override val startedAt: FuzzyDateModel? = null,
        @SerialName("status") override val status: MediaListStatus? = null,
        @SerialName("updatedAt") override val updatedAt: Long? = null,
        @SerialName("user") override val user: UserModel.Core,
        @SerialName("id") override val id: Long,
    ) : MediaListModel()

    /**
     * @param media The media for the list entry
     */
    @Serializable
    internal data class Extended(
        @SerialName("media") val media: MediaModel.Media,
        @SerialName("advancedScores") override val advancedScores: Map<String, Float>? = null,
        @SerialName("customLists") override val customLists: List<CustomList>? = null,
        @SerialName("completedAt") override val completedAt: FuzzyDateModel? = null,
        @SerialName("createdAt") override val createdAt: Long,
        @SerialName("hiddenFromStatusLists") override val hiddenFromStatusLists: Boolean? = null,
        @SerialName("mediaId") override val mediaId: Long,
        @SerialName("notes") override val notes: String? = null,
        @SerialName("priority") override val priority: Int? = null,
        @SerialName("private") override val private: Boolean? = null,
        @SerialName("progress") override val progress: Int? = null,
        @SerialName("progressVolumes") override val progressVolumes: Int? = null,
        @SerialName("repeat") override val repeat: Int? = null,
        @SerialName("score") override val score: Float? = null,
        @SerialName("startedAt") override val startedAt: FuzzyDateModel? = null,
        @SerialName("status") override val status: MediaListStatus? = null,
        @SerialName("updatedAt") override val updatedAt: Long? = null,
        @SerialName("user") override val user: UserModel.Core,
        @SerialName("id") override val id: Long
    ) : MediaListModel()
}