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

package co.anitrend.domain.media.enums

/**
 * Type of relation media has to its parent
 */
enum class MediaRelation {
    /** An adaption of this media into a different format */
    ADAPTATION,
    /** An alternative version of the same media */
    ALTERNATIVE,
    /** Shares at least 1 character */
    CHARACTER,
    /** Version 2 only */
    COMPILATION,
    /** Version 2 only */
    CONTAINS,
    /** Other */
    OTHER,
    /** The media a side story is from */
    PARENT,
    /** Released before the relation */
    PREQUEL,
    /** Released after the relation */
    SEQUEL,
    /** A side story of the parent media */
    SIDE_STORY,
    /** Version 2 only. The source material the media was adapted from */
    SOURCE,
    /** An alternative version of the media with a different primary focus */
    SPIN_OFF,
    /** A shortened and summarized version */
    SUMMARY
}