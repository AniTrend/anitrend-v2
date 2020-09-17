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
 * Source type the media was adapted from
 */
enum class MediaSource(override val alias: CharSequence) : IAliasable {
    /** Version 2 only. Japanese Anime */
    ANIME("Anime"),
    /** Version 2 only. Self-published works */
    DOUJINSHI("Doujinshi"),
    /** Written work published in volumes */
    LIGHT_NOVEL("Light novel"),
    /** Asian comic book */
    MANGA("Manga"),
    /** Version 2 only. Written works not published in volumes */
    NOVEL("Novel"),
    /** An original production not based of another work */
    ORIGINAL("Original"),
    /** Other */
    OTHER("Other"),
    /** Video game */
    VIDEO_GAME("Video game"),
    /** Video game driven primary by text and narrative */
    VISUAL_NOVEL("Visual novel")
}