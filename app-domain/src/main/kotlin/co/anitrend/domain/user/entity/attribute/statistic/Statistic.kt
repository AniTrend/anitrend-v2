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

package co.anitrend.domain.user.entity.attribute.statistic

import co.anitrend.domain.user.entity.attribute.statistic.contract.IStatistic

sealed class Statistic : IStatistic {

    abstract val count: Int
    abstract val meanScore: Float
    abstract val standardDeviation: Float

    data class Anime(
        val minutesWatched: Int,
        val episodesWatched: Int,
        override val count: Int,
        override val meanScore: Float,
        override val standardDeviation: Float,
        override val countries: List<MediaStatistic.Anime>?,
        override val formats: List<MediaStatistic.Anime>?,
        override val genres: List<MediaStatistic.Anime>?,
        override val lengths: List<MediaStatistic.Anime>?,
        override val releaseYears: List<MediaStatistic.Anime>?,
        override val scores: List<MediaStatistic.Anime>?,
        override val staff: List<MediaStatistic.Anime>?,
        override val startYears: List<MediaStatistic.Anime>?,
        override val statuses: List<MediaStatistic.Anime>?,
        override val studios: List<MediaStatistic.Anime>?,
        override val tags: List<MediaStatistic.Anime>?,
        override val voiceActors: List<MediaStatistic.Anime>?
    ) : Statistic()

    data class Manga(
        val chaptersRead: Int,
        val volumesRead: Int,
        override val count: Int,
        override val meanScore: Float,
        override val standardDeviation: Float,
        override val countries: List<MediaStatistic.Manga>?,
        override val formats: List<MediaStatistic.Manga>?,
        override val genres: List<MediaStatistic.Manga>?,
        override val lengths: List<MediaStatistic.Manga>?,
        override val releaseYears: List<MediaStatistic.Manga>?,
        override val scores: List<MediaStatistic.Manga>?,
        override val staff: List<MediaStatistic.Manga>?,
        override val startYears: List<MediaStatistic.Manga>?,
        override val statuses: List<MediaStatistic.Manga>?,
        override val studios: List<MediaStatistic.Manga>?,
        override val tags: List<MediaStatistic.Manga>?,
        override val voiceActors: List<MediaStatistic.Manga>?
    ) : Statistic()
}