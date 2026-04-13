/*
 * Copyright (C) 2026 AniTrend
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
package co.anitrend.domain.common.extension

import co.anitrend.domain.medialist.enums.ScoreFormat
import java.util.Locale

fun Int.asFormattedScore(scoreFormat: ScoreFormat): String =
    when (scoreFormat) {
        ScoreFormat.POINT_100 -> "$this/100"
        ScoreFormat.POINT_10 -> "${this / 10}/10"
        ScoreFormat.POINT_10_DECIMAL -> String.format(Locale.US, "%.1f/10", this / 10f)
        ScoreFormat.POINT_5 -> "${this * 5 / 100}/5"
        ScoreFormat.POINT_3 ->
            when (this) {
                in 67..100 -> "3/3"
                in 34..66 -> "2/3"
                in 1..33 -> "1/3"
                else -> "0/3"
            }
    }

