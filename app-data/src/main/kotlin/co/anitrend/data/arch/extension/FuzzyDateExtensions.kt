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

package co.anitrend.data.arch.extension

import co.anitrend.data.arch.common.model.date.FuzzyDateModel
import co.anitrend.data.arch.FuzzyDateInt
import co.anitrend.data.arch.FuzzyDateLike
import co.anitrend.domain.common.entity.shared.FuzzyDate

internal fun FuzzyDateModel?.asFuzzyDate(): FuzzyDate {
    val model = this ?: FuzzyDateModel.empty()
    return FuzzyDate(
        year = model.year,
        month = model.month,
        day = model.day
    )
}

internal fun FuzzyDate?.asFuzzyDateModel(): FuzzyDateModel {
    val model = this ?: FuzzyDate.empty()
    return FuzzyDateModel(
        year = model.year,
        month = model.month,
        day = model.day
    )
}

/** [FuzzyDateInt](https://anilist.github.io/ApiV2-GraphQL-Docs/fuzzydateint.doc.html)
 *
 * @see FuzzyDateInt
 */
internal fun FuzzyDateModel.toFuzzyDateInt() : FuzzyDateInt {
    if (isDateNotSet())
        return "0"

    val fuzzyDateYear = when {
        year < 10 -> "000$year"
        year < 100 -> "00$year"
        year < 1000 -> "0$year"
        else -> "$year"
    }
    val fuzzyDateMonth = when {
        month <= 9 -> "0$month"
        else -> "$month"
    }
    val fuzzyDateDay = when {
        day <= 9 -> "0$day"
        else -> "$day"
    }
    return "$fuzzyDateYear$fuzzyDateMonth$fuzzyDateDay"
}

/** [FuzzyDateModel](https://anilist.github.io/ApiV2-GraphQL-Docs/fuzzydate.doc.html)
 *
 * @see FuzzyDateModel
 */
internal fun FuzzyDateInt?.toFuzzyDateModel() : FuzzyDateModel {
    if (this == "0" || this == null)
        return FuzzyDateModel.empty()

    val fuzzyDateIntYear = substring(0, 4).toInt()
    val fuzzyDateIntMonth = substring(4, 6).toInt()
    val fuzzyDateIntDay = substring(6, 8).toInt()

    return FuzzyDateModel(
        year = fuzzyDateIntYear,
        month = fuzzyDateIntMonth,
        day = fuzzyDateIntDay
    )
}


internal fun FuzzyDateModel.toFuzzyDateLike() : FuzzyDateLike {
    if (isDateNotSet())
        return "0"

    val fuzzyDateYear = when {
        year == 0 -> "%"
        year < 10 -> "000$year"
        year < 100 -> "00$year"
        year < 1000 -> "0$year"
        else -> "$year"
    }
    val fuzzyDateMonth = when {
        month == 0 -> {
            if (fuzzyDateYear != "%")
                "%"
            else
                ""
        }
        month <= 9 -> "0$month"
        else -> "$month"
    }
    val fuzzyDateDay = when {
        day == 0 -> {
            if (fuzzyDateMonth != "%")
                "%"
            else
                ""
        }
        day <= 9 -> "0$day"
        else -> "$day"
    }

    return "$fuzzyDateYear$fuzzyDateMonth$fuzzyDateDay"
}