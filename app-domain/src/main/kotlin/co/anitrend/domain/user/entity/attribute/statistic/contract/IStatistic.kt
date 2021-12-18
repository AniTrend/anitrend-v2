/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.domain.user.entity.attribute.statistic.contract

import co.anitrend.domain.user.entity.attribute.statistic.MediaStatistic

interface IStatistic {
    val countries: List<MediaStatistic>?
    val formats: List<MediaStatistic>?
    val genres: List<MediaStatistic>?
    val lengths: List<MediaStatistic>?
    val releaseYears: List<MediaStatistic>?
    val scores: List<MediaStatistic>?
    val staff: List<MediaStatistic>?
    val startYears: List<MediaStatistic>?
    val statuses: List<MediaStatistic>?
    val studios: List<MediaStatistic>?
    val tags: List<MediaStatistic>?
    val voiceActors: List<MediaStatistic>?
}