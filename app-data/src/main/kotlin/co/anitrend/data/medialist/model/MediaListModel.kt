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

import co.anitrend.data.arch.common.model.date.FuzzyDateModel
import co.anitrend.data.media.model.MediaModel
import co.anitrend.data.medialist.model.contract.IMediaListModel
import co.anitrend.data.user.model.remote.UserModel
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
        @SerialName("advancedScores") override val advancedScores: Map<String, Float>?,
        @SerialName("customLists") override val customLists: List<CustomList>?,
        @SerialName("completedAt") override val completedAt: FuzzyDateModel?,
        @SerialName("createdAt") override val createdAt: Long,
        @SerialName("hiddenFromStatusLists") override val hiddenFromStatusLists: Boolean?,
        @SerialName("mediaId") override val mediaId: Long,
        @SerialName("notes") override val notes: String?,
        @SerialName("priority") override val priority: Int?,
        @SerialName("private") override val private: Boolean?,
        @SerialName("progress") override val progress: Int?,
        @SerialName("progressVolumes") override val progressVolumes: Int?,
        @SerialName("repeat") override val repeat: Int?,
        @SerialName("score") override val score: Float?,
        @SerialName("startedAt") override val startedAt: FuzzyDateModel?,
        @SerialName("status") override val status: MediaListStatus?,
        @SerialName("updatedAt") override val updatedAt: Long?,
        @SerialName("userId") override val userId: Long,
        @SerialName("id") override val id: Long,
    ) : MediaListModel()

    /**
     * @param media The media for the list entry
     */
    @Serializable
    internal data class Extended(
        @SerialName("media") val media: MediaModel.Media,
        @SerialName("advancedScores") override val advancedScores: Map<String, Float>?,
        @SerialName("customLists") override val customLists: List<CustomList>?,
        @SerialName("completedAt") override val completedAt: FuzzyDateModel?,
        @SerialName("createdAt") override val createdAt: Long,
        @SerialName("hiddenFromStatusLists") override val hiddenFromStatusLists: Boolean?,
        @SerialName("mediaId") override val mediaId: Long,
        @SerialName("notes") override val notes: String?,
        @SerialName("priority") override val priority: Int?,
        @SerialName("private") override val private: Boolean?,
        @SerialName("progress") override val progress: Int?,
        @SerialName("progressVolumes") override val progressVolumes: Int?,
        @SerialName("repeat") override val repeat: Int?,
        @SerialName("score") override val score: Float?,
        @SerialName("startedAt") override val startedAt: FuzzyDateModel?,
        @SerialName("status") override val status: MediaListStatus?,
        @SerialName("updatedAt") override val updatedAt: Long?,
        @SerialName("userId") override val userId: Long,
        @SerialName("id") override val id: Long
    ) : MediaListModel()


    /** [MediaListCollection](https://anilist.github.io/ApiV2-GraphQL-Docs/medialistcollection.doc.html)
     * List of anime or manga
     *
     * @param lists Grouped media list entries
     * @param hasNextChunk If there is another chunk
     * @param user The owner of the list
     */
    @Serializable
    internal data class Collection(
        @SerialName("lists") val lists: List<Group>,
        @SerialName("hasNextChunk") val hasNextChunk: Boolean,
        @SerialName("user") val user: UserModel.Core?
    ) {

        /** [MediaListGroup](https://anilist.github.io/ApiV2-GraphQL-Docs/medialistgroup.doc.html)
         * List group of anime or manga entries
         *
         * @param entries Media list entries
         * @param name name of the current group
         * @param isCustomList If the current group is a custom list
         * @param isSplitCompletedList If this grouping is split by types of of media, e.g movies, tv, specials, etc
         * @param status status of current group, one of [MediaListStatus]
         */
        @Serializable
        internal data class Group(
            @SerialName("entries") val entries: List<Extended>?,
            @SerialName("isCustomList") val isCustomList: Boolean,
            @SerialName("isSplitCompletedList") val isSplitCompletedList: Boolean,
            @SerialName("name") val name: String,
            @SerialName("status") val status: MediaListStatus
        )
    }
}