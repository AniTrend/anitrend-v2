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

package co.anitrend.data.cache.helper

import org.threeten.bp.Instant
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.Period
import org.threeten.bp.temporal.ChronoUnit
import org.threeten.bp.temporal.TemporalAmount

internal fun TemporalAmount.inPast(): Instant = Instant.now().minus(this)

internal fun periodOf(years: Int = 0, months: Int = 0, days: Int = 0) = Period.of(years, months, days)

internal fun instantInPast(days: Int = 0, hours: Int = 0, minutes: Int = 0): Instant {
    var instant = Instant.now()
    if (days != 0) {
        instant = instant.minus(days.toLong(), ChronoUnit.DAYS)
    }
    if (hours != 0) {
        instant = instant.minus(hours.toLong(), ChronoUnit.HOURS)
    }
    if (minutes != 0) {
        instant = instant.minus(minutes.toLong(), ChronoUnit.HOURS)
    }
    return instant
}

internal fun OffsetDateTime.isBefore(instant: Instant): Boolean = toInstant().isBefore(instant)
