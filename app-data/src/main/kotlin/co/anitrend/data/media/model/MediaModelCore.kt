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

package co.anitrend.data.media.model

import co.anitrend.data.airing.model.AiringScheduleModel
import co.anitrend.data.media.model.contract.IMediaModelCore
import co.anitrend.data.medialist.model.remote.MediaListModelCore
import co.anitrend.domain.common.entity.shared.FuzzyDate
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaSeason
import co.anitrend.domain.media.enums.MediaStatus
import co.anitrend.domain.media.enums.MediaType
import com.google.gson.annotations.SerializedName

internal data class MediaModelCore(
    @SerializedName("title") override val title: MediaModelExtended.Title?,
    @SerializedName("bannerImage") override val bannerImage: String?,
    @SerializedName("coverImage") override val coverImage: MediaModelExtended.CoverImage?,
    @SerializedName("type") override val type: MediaType?,
    @SerializedName("format") override val format: MediaFormat?,
    @SerializedName("season") override val season: MediaSeason?,
    @SerializedName("status") override val status: MediaStatus?,
    @SerializedName("meanScore") override val meanScore: Int?,
    @SerializedName("averageScore") override val averageScore: Int?,
    @SerializedName("startDate") override val startDate: FuzzyDate?,
    @SerializedName("endDate") override val endDate: FuzzyDate?,
    @SerializedName("episodes") override val episodes: Int?,
    @SerializedName("duration") override val duration: Int?,
    @SerializedName("chapters") override val chapters: Int?,
    @SerializedName("volumes") override val volumes: Int?,
    @SerializedName("isAdult") override val isAdult: Boolean?,
    @SerializedName("isFavourite") override val isFavourite: Boolean?,
    @SerializedName("nextAiringEpisode") override val nextAiringEpisode: AiringScheduleModel?,
    @SerializedName("mediaListEntry") override val mediaListEntry: MediaListModelCore?,
    @SerializedName("id") override val id: Long
) : IMediaModelCore