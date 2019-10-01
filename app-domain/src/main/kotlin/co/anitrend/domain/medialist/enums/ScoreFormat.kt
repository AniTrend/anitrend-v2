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

package co.anitrend.domain.medialist.enums

/**
 * Media list scoring type
 */
enum class ScoreFormat {
    /** An integer from 0-10 */
    POINT_10,
    /** An integer from 0-100 */
    POINT_100,
    /** A float from 0-10 with 1 decimal place */
    POINT_10_DECIMAL,
    /** An integer from 0-3. Should be represented in Smileys. 0 => No Score, 1 => :(, 2 => :|, 3 => :) */
    POINT_3,
    /** An integer from 0-5. Should be represented in Stars */
    POINT_5
}