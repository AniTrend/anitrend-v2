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
 * 8 digit long date integer (YYYYMMDD).
 * Unknown dates represented by 0.
 *
 * E.g. 2016: 20160000
 * May 1976: 19760500
 */
typealias FuzzyDateInt = String

/**
 * A query filter type for FuzzyDateInt,
 * instead of return YYYYMMDD any unset fields
 * are replaced by %
 *
 * E.g 2019: 2019%
 * May 2011: 201105%
 */
typealias FuzzyDateLike = String

/**
 * [ISO 3166-1 alpha-2](https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2) Two-letter country codes
 */
typealias CountryCode = String