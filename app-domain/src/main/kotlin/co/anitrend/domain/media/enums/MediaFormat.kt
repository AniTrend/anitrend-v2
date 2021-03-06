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
 * The format the media was released in
 */
enum class MediaFormat(override val alias: CharSequence) : IAliasable {
    /** Professionally published manga with more than one chapter */
    MANGA("Manga"),
    /** Anime movies with a theatrical release */
    MOVIE("Movie"),
    /** Short anime released as a music video */
    MUSIC("Music"),
    /** Written books released as a series of light novels */
    NOVEL("Novel"),
    /** (Original Net Animation) Anime that have been originally released online
     * or are only available through streaming services
     */
    ONA("ONA"),
    /** Manga with just one chapter */
    ONE_SHOT("One Shot"),
    /** (Original Video Animation) Anime that have been released directly on
     * DVD/Blu-ray without originally going through a theatrical release
     * or television broadcast
     */
    OVA("OVA"),
    /** Special episodes that have been included in DVD/Blu-ray releases,
     * picture dramas, pilots, etc
     */
    SPECIAL("Special"),
    /** Anime broadcast on television */
    TV("TV"),
    /** Anime which are under 15 minutes in length and broadcast on */
    TV_SHORT("TV Short");

    companion object {
        /** If a media format can be represented with a quantity */
        fun MediaFormat?.isQuantitative(): Boolean =
            this == MANGA || this == TV || this == SPECIAL ||
                    this == NOVEL || this == TV_SHORT
    }
}