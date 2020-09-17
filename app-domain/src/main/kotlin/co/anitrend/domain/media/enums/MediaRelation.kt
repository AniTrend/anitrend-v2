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

import co.anitrend.domain.common.enums.contract.IAliasable

/**
 * Type of relation media has to its parent
 */
enum class MediaRelation(override val alias: CharSequence) : IAliasable {
    /** An adaption of this media into a different format */
    ADAPTATION("Adaptation"),
    /** An alternative version of the same media */
    ALTERNATIVE("Alternative"),
    /** Shares at least 1 character */
    CHARACTER("Shares cast"),
    /** Version 2 only */
    COMPILATION("Compilation"),
    /** Version 2 only */
    CONTAINS("Contains"),
    /** Other */
    OTHER("Other"),
    /** The media a side story is from */
    PARENT("Parent"),
    /** Released before the relation */
    PREQUEL("Prequel"),
    /** Released after the relation */
    SEQUEL("Sequel"),
    /** A side story of the parent media */
    SIDE_STORY("Side story"),
    /** Version 2 only. The source material the media was adapted from */
    SOURCE("Source"),
    /** An alternative version of the media with a different primary focus */
    SPIN_OFF("Spin off"),
    /** A shortened and summarized version */
    SUMMARY("Summary")
}