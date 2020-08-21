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

package co.anitrend.domain.media.entity.contract

import co.anitrend.domain.airing.entity.AiringSchedule
import co.anitrend.domain.media.enums.MediaType

/**
 * A category type of media
 *
 * @param type A [MediaType] enum
 */
sealed class MediaCategory(
    val type: MediaType,
) {
    /**
     * Japanese Anime
     *
     * @param episodes The amount of episodes the anime has when complete
     * @param duration The general length of each anime episode in minutes
     * @param schedule The media's next episode airing schedule
     */
    data class Anime(
        val episodes: UShort,
        val duration: UShort,
        val schedule: AiringSchedule?
    ) : MediaCategory(
        MediaType.ANIME
    )

    /**
     * Asian comic
     *
     * @param chapters The amount of chapters the manga has when complete
     * @param volumes The amount of volumes the manga has when complete
     */
    data class Manga(
        val chapters: UShort,
        val volumes: UShort
    ) : MediaCategory(
        MediaType.MANGA
    )
}