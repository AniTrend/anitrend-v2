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

package co.anitrend.domain.common

/**
 * A hex colour character sequence, that can be any of the following:
 * - #RRGGBB
 * - #AARRGGBB
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