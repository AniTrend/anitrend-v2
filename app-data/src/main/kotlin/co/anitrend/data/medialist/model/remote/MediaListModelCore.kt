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

package co.anitrend.data.medialist.model.remote

import co.anitrend.data.arch.common.model.date.FuzzyDateModel
import co.anitrend.data.medialist.model.contract.IMediaListModelCore

import co.anitrend.domain.medialist.enums.MediaListStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** [MediaList](https://anilist.github.io/ApiV2-GraphQL-Docs/medialist.doc.html)
 * List of anime or manga
 */
@Serializable
internal data class MediaListModelCore(
    @SerialName("advancedScores") override val advancedScores: Map<String, String>?,
    @SerialName("completedAt") override val completedAt: FuzzyDateModel?,
    @SerialName("createdAt") override val createdAt: Long?,
    @SerialName("customLists") override val customLists: Map<String, String>?,
    @SerialName("hiddenFromStatusLists") override val hiddenFromStatusLists: Boolean?,
    @SerialName("mediaId") override val mediaId: Int,
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
    @SerialName("userId") override val userId: Int,
    @SerialName("id") override val id: Long
) : IMediaListModelCore