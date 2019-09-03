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

package co.anitrend.domain.enums.medialist

import co.anitrend.domain.common.enum.IGraphEnum

/**
 * Media list watching/reading status enum
 */
enum class MediaListStatus(override val value: String) : IGraphEnum {
    /** Finished watching/reading */
    COMPLETED("COMPLETED"),
    /** Currently watching/reading */
    CURRENT("CURRENT"),
    /** Stopped watching/reading before completing */
    DROPPED("DROPPED"),
    /** Paused watching/reading */
    PAUSED("PAUSED"),
    /** Planning to watch/read */
    PLANNING("PLANNING"),
    /** Re-watching/reading */
    REPEATING("REPEATING")
}