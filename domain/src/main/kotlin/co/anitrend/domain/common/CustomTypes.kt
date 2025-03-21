/*
 * Copyright (C) 2019 AniTrend
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
package co.anitrend.domain.common

/**
 * A hex colour character sequence, that can be any of the following:
 * - `#RRGGBB`
 * - `#AARRGGBB`
 */
typealias HexColor = CharSequence

/**
 * 8 digit long date integer (YYYYMMDD).
 * Unknown dates represented by 0.
 *
 * > 2016: 20160000
 * > May 1976: 19760500
 */
typealias DateInt = CharSequence

/**
 * A query filter type for a fuzzy date,
 * instead of return YYYYMMDD any unset fields
 * are replaced by %
 *
 * > 2019: 2019%
 * > May 2011: 201105%
 */
typealias DateLike = CharSequence

/**
 * [ISO 3166-1 alpha-2](https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2) Two-letter country codes
 */
internal typealias CountryCode = CharSequence

/**
 * Converts a [Int] to a DateInt (YYYYMMDD integer).
 *
 * Pads partial dates (at least 4 digits) with '0' to 8 digits.
 *
 * @receiver The [Int] to convert.
 * @return A [DateInt] representing the parsed date.
 * @throws IllegalArgumentException if the CharSequence has less than 4 digits or contains non-digit characters.
 */
fun Int.toDateInt(): DateInt {
    require(this >= 1000) { "Invalid date format. Expected at least 4 digits (YYYY), but got: $this" }
    return toString().padEnd(8, '0')
}

/**
 * Converts a [DateInt] to [Int].
 *
 * @receiver The [DateInt] to convert.
 * @throws IllegalArgumentException if the CharSequence has less than 4 digits or contains non-digit characters.
 */
fun DateInt.fromDateInt(trim: Int = 1): Int {
    require(length >= 4) { "Invalid date format. Expected at least 4 digits (YYYY), but got: $this" }
    require(all(Char::isDigit)) { "Invalid date format. Expected only digits, but got: $this" }
    return Integer.parseInt(toString()) / trim
}
