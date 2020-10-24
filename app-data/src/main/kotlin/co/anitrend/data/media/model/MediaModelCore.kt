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
import co.anitrend.data.arch.common.model.date.FuzzyDateModel
import co.anitrend.data.media.model.contract.IMediaModelCore
import co.anitrend.data.medialist.model.remote.MediaListModelCore
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaSeason
import co.anitrend.domain.media.enums.MediaStatus
import co.anitrend.domain.media.enums.MediaType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class MediaModelCore(
    @SerialName("title") override val title: MediaModelExtended.Title?,
    @SerialName("bannerImage") override val bannerImage: String?,
    @SerialName("coverImage") override val coverImage: MediaModelExtended.CoverImage?,
    @SerialName("type") override val type: MediaType?,
    @SerialName("format") override val format: MediaFormat?,
    @SerialName("season") override val season: MediaSeason?,
    @SerialName("status") override val status: MediaStatus?,
    @SerialName("meanScore") override val meanScore: Int?,
    @SerialName("averageScore") override val averageScore: Int?,
    @SerialName("startDate") override val startDate: FuzzyDateModel?,
    @SerialName("endDate") override val endDate: FuzzyDateModel?,
    @SerialName("episodes") override val episodes: Int?,
    @SerialName("duration") override val duration: Int?,
    @SerialName("chapters") override val chapters: Int?,
    @SerialName("volumes") override val volumes: Int?,
    @SerialName("isAdult") override val isAdult: Boolean?,
    @SerialName("isFavourite") override val isFavourite: Boolean?,
    @SerialName("nextAiringEpisode") override val nextAiringEpisode: AiringScheduleModel?,
    @SerialName("mediaListEntry") override val mediaListEntry: MediaListModelCore?,
    @SerialName("id") override val id: Long
) : IMediaModelCore