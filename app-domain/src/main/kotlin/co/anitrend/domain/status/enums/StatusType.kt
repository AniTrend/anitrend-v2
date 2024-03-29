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

package co.anitrend.domain.status.enums

import co.anitrend.domain.common.enums.contract.IAliasable

/**
 * Activity type enum
 */
enum class StatusType(override val alias: CharSequence) : IAliasable {
    /** A text activity */
    TEXT("Text"),
    /** A anime list update activity */
    ANIME_LIST("Anime List"),
    /** A manga list update activity */
    MANGA_LIST("Manga List"),
    /** A text message activity sent to another user */
    MESSAGE("Message"),
    /** Anime & Manga list update, only used in query arguments */
    MEDIA_LIST("Media List")
}