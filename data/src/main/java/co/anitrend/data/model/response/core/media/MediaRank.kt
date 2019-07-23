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

package co.anitrend.data.model.response.core.media

import co.anitrend.data.usecase.media.attributes.MediaFormat
import co.anitrend.data.usecase.media.attributes.MediaRankType
import co.anitrend.data.usecase.media.attributes.MediaSeason

/** [MediaRank](https://anilist.github.io/ApiV2-GraphQL-Docs/mediarank.doc.html)
 * The ranking of a media in a particular time span and format compared to other media
 *
 * @param allTime If the ranking is based on all time instead of a season/year
 * @param context String that gives context to the ranking type and time span
 * @param format The format the media is ranked within
 * @param id The id of the rank
 * @param rank The numerical rank of the media
 * @param season The season the media is ranked within
 * @param type The type of ranking
 * @param year The year the media is ranked within
 */
data class MediaRank(
    val allTime: Boolean?,
    val context: String,
    val format: MediaFormat,
    val id: Int,
    val rank: Int,
    val season: MediaSeason?,
    val type: MediaRankType,
    val year: Int?
)