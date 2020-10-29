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

package co.anitrend.data.medialist.model.remote

import co.anitrend.data.arch.common.model.date.FuzzyDateModel
import co.anitrend.data.media.model.contract.IMediaModelCore
import co.anitrend.data.medialist.model.contract.IMediaListModelExtended
import co.anitrend.domain.medialist.enums.MediaListStatus
import com.google.gson.annotations.SerializedName

internal data class MediaListModelExtended(
    @SerializedName("media") override val media: IMediaModelCore?,
    @SerializedName("advancedScores") override val advancedScores: Map<String, String>?,
    @SerializedName("customLists") override val customLists: Map<String, String>?,
    @SerializedName("completedAt") override val completedAt: FuzzyDateModel?,
    @SerializedName("createdAt") override val createdAt: Long?,
    @SerializedName("hiddenFromStatusLists") override val hiddenFromStatusLists: Boolean?,
    @SerializedName("mediaId") override val mediaId: Int,
    @SerializedName("notes") override val notes: String?,
    @SerializedName("priority") override val priority: Int?,
    @SerializedName("private") override val private: Boolean?,
    @SerializedName("progress") override val progress: Int?,
    @SerializedName("progressVolumes") override val progressVolumes: Int?,
    @SerializedName("repeat") override val repeat: Int?,
    @SerializedName("score") override val score: Float?,
    @SerializedName("startedAt") override val startedAt: FuzzyDateModel?,
    @SerializedName("status") override val status: MediaListStatus?,
    @SerializedName("updatedAt") override val updatedAt: Long?,
    @SerializedName("userId") override val userId: Int,
    @SerializedName("id") override val id: Long
) : IMediaListModelExtended