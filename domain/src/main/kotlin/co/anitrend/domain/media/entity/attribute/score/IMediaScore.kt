/*
 * Copyright (C) 2025 AniTrend
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
package co.anitrend.domain.media.entity.attribute.score

import co.anitrend.domain.medialist.enums.ScoreFormat

interface IMediaScore {
    val mean: Int
    val average: Int
    val personal: Float?
    val popularity: Int?
    val trending: Int?

    fun asFormatted(scoreFormat: ScoreFormat): IMediaRating

    fun asFormattedPersonal(scoreFormat: ScoreFormat): IMediaRating?

    fun asFormattedCommunity(scoreFormat: ScoreFormat): IMediaRating
}
