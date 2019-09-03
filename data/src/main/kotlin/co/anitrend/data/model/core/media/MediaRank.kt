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

package co.anitrend.data.model.core.media

import co.anitrend.domain.entities.response.media.IMediaRank
import co.anitrend.domain.enums.media.MediaFormat
import co.anitrend.domain.enums.media.MediaRankType
import co.anitrend.domain.enums.media.MediaSeason

/** [MediaRank](https://anilist.github.io/ApiV2-GraphQL-Docs/mediarank.doc.html)
 * The ranking of a media in a particular time span and format compared to other media
 */
data class MediaRank(
    override val allTime: Boolean?,
    override val context: String,
    override val format: MediaFormat,
    override val rank: Int,
    override val season: MediaSeason?,
    override val type: MediaRankType,
    override val year: Int?,
    override val id: Long
) : IMediaRank