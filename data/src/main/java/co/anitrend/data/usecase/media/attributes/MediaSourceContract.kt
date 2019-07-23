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

package co.anitrend.data.usecase.media.attributes

import androidx.annotation.StringDef

/**
 * Source type the media was adapted from
 */
@StringDef(
    MediaSourceContract.ANIME,
    MediaSourceContract.DOUJINSHI,
    MediaSourceContract.LIGHT_NOVEL,
    MediaSourceContract.MANGA,
    MediaSourceContract.NOVEL,
    MediaSourceContract.ORIGINAL,
    MediaSourceContract.OTHER,
    MediaSourceContract.VIDEO_GAME,
    MediaSourceContract.VISUAL_NOVEL
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPEALIAS)
annotation class MediaSourceContract {
    companion object {
        /** Version 2 only. Japanese Anime */
        const val ANIME = "ANIME"
        /** Version 2 only. Self-published works */
        const val DOUJINSHI = "DOUJINSHI"
        /** Written work published in volumes */
        const val LIGHT_NOVEL = "LIGHT_NOVEL"
        /** Asian comic book */
        const val MANGA = "MANGA"
        /** Version 2 only. Written works not published in volumes */
        const val NOVEL = "NOVEL"
        /** An original production not based of another work */
        const val ORIGINAL = "ORIGINAL"
        /** Other */
        const val OTHER = "OTHER"
        /** Video game */
        const val VIDEO_GAME = "VIDEO_GAME"
        /** Video game driven primary by text and narrative */
        const val VISUAL_NOVEL = "VISUAL_NOVEL"
        
        val ALL = listOf(
            ANIME,
            DOUJINSHI,
            LIGHT_NOVEL,
            MANGA,
            NOVEL,
            ORIGINAL,
            OTHER,
            VIDEO_GAME,
            VISUAL_NOVEL
        )
    }
}

typealias MediaSource = String