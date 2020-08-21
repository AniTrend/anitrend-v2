/*
 * Copyright (C) 2020  AniTrend
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

package co.anitrend.domain.medialist.entity.contract

sealed class MediaListProgress(
    val progress: UShort = 0u
) {
    data class Anime(
        val episodeProgress: UShort = 0u,
        val repeatedCount: UShort = 0u
    ) : MediaListProgress(episodeProgress)

    data class Manga(
        val chapterProgress: UShort = 0u,
        val volumeProgress: UShort = 0u,
        val repeatedCount: UShort = 0u
    ) : MediaListProgress(chapterProgress)
}