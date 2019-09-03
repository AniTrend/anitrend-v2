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

package co.anitrend.domain.enums.media

import co.anitrend.domain.common.enum.IGraphEnum

/**
 * Type of relation media has to its parent
 */
enum class MediaRelation(override val value: String) : IGraphEnum {
    /** An adaption of this media into a different format */
    ADAPTATION("ADAPTATION"),
    /** An alternative version of the same media */
    ALTERNATIVE("ALTERNATIVE"),
    /** Shares at least 1 character */
    CHARACTER("CHARACTER"),
    /** Version 2 only */
    COMPILATION("COMPILATION"),
    /** Version 2 only */
    CONTAINS("CONTAINS"),
    /** Other */
    OTHER("OTHER"),
    /** The media a side story is from */
    PARENT("PARENT"),
    /** Released before the relation */
    PREQUEL("PREQUEL"),
    /** Released after the relation */
    SEQUEL("SEQUEL"),
    /** A side story of the parent media */
    SIDE_STORY("SIDE_STORY"),
    /** Version 2 only. The source material the media was adapted from */
    SOURCE("SOURCE"),
    /** An alternative version of the media with a different primary focus */
    SPIN_OFF("SPIN_OFF"),
    /** A shortened and summarized version */
    SUMMARY("SUMMARY")
}