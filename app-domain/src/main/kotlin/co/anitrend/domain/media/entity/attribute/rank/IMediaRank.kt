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

package co.anitrend.domain.media.entity.attribute.rank

import co.anitrend.domain.common.entity.contract.IEntity
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaRankType
import co.anitrend.domain.media.enums.MediaSeason

/** [MediaRank](https://anilist.github.io/ApiV2-GraphQL-Docs/mediarank.doc.html)
 * Media rank contract
 *
 * @property allTime If the ranking is based on all time instead of a season/year
 * @property context String that gives context to the ranking type and time span
 * @property format The format the media is ranked within
 * @property rank The numerical rank of the media
 * @property season The season the media is ranked within
 * @property type The type of ranking
 * @property year The year the media is ranked within
 */
interface IMediaRank : IEntity {
    val allTime: Boolean?
    val context: CharSequence
    val format: MediaFormat
    val rank: Int
    val season: MediaSeason?
    val type: MediaRankType
    val year: Int?
}