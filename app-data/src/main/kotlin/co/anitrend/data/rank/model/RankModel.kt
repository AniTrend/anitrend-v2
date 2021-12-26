/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.data.rank.model

import co.anitrend.data.core.common.Identity
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaRankType
import co.anitrend.domain.media.enums.MediaSeason
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** [MediaRank](https://anilist.github.io/ApiV2-GraphQL-Docs/mediarank.doc.html)
 * The ranking of a media in a particular time span and format compared to other media
 */
@Serializable
internal data class RankModel(
    @SerialName("allTime") val allTime: Boolean? = null,
    @SerialName("context") val context: String,
    @SerialName("format") val format: MediaFormat,
    @SerialName("rank") val rank: Int,
    @SerialName("season") val season: MediaSeason? = null,
    @SerialName("type") val type: MediaRankType,
    @SerialName("year") val year: Int? = null,
    @SerialName("id") override val id: Long
) : Identity