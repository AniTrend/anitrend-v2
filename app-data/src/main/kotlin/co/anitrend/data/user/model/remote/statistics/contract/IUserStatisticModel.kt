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

package co.anitrend.data.user.model.remote.statistics.contract

import co.anitrend.data.user.model.remote.statistics.media.IStatisticModel

/** [UserStatistics](https://anilist.github.io/ApiV2-GraphQL-Docs/UserStatistics.doc.html)
 *
 * Contract for user statistics without any anime or manga information
 *
 * @property count TBA
 * @property countries TBA
 * @property formats TBA
 * @property genres TBA
 * @property lengths TBA
 * @property releaseYears TBA
 * @property scores TBA
 * @property staff TBA
 * @property startYears TBA
 * @property statuses TBA
 * @property studios TBA
 * @property tags TBA
 * @property voiceActors TBA
 */
internal interface IUserStatisticModel {
    val countries: List<IStatisticModel>?
    val formats: List<IStatisticModel>?
    val genres: List<IStatisticModel>?
    val lengths: List<IStatisticModel>?
    val releaseYears: List<IStatisticModel>?
    val scores: List<IStatisticModel>?
    val staff: List<IStatisticModel>?
    val startYears: List<IStatisticModel>?
    val statuses: List<IStatisticModel>?
    val studios: List<IStatisticModel>?
    val tags: List<IStatisticModel>?
    val voiceActors: List<IStatisticModel>?
}