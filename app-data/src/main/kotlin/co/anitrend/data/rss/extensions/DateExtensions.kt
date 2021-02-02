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

package co.anitrend.data.rss.extensions

import co.anitrend.arch.extension.util.date.contract.ISupportDateHelper
import co.anitrend.data.arch.extension.koinOf
import co.anitrend.data.rss.ISO8601Date
import co.anitrend.data.rss.RCF822Date
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber


internal const val ISO8601_PATTERN = "yyyy-MM-dd'T'HH:mm:ssXXX"


internal fun ISO8601Date.iso8601ToUnixTime() =
    runCatching{
        koinOf<ISupportDateHelper>()
            .convertToUnixTimeStamp(
                originDate = this,
                dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
            )
    }.getOrElse {
        Timber.tag("iso8601ToUnixTime").e(it)
        0
    }

internal fun RCF822Date.rcf822ToUnixTime() =
    runCatching {
        koinOf<ISupportDateHelper>()
            .convertToUnixTimeStamp(
                originDate = this,
                dateTimeFormatter = DateTimeFormatter.RFC_1123_DATE_TIME
            )
    }.getOrElse {
        Timber.tag("rcf822ToUnixTime").e(it)
        0
    }