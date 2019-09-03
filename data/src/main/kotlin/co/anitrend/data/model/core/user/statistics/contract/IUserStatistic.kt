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

package co.anitrend.data.model.core.user.statistics.contract

import co.anitrend.data.model.core.user.statistics.*

/** [UserStatistics](https://anilist.github.io/ApiV2-GraphQL-Docs/UserStatistics.doc.html)
 *
 * Contract for user statistics without any anime or manga information
 *
 * @property count TBA
 * @property countries TBA
 * @property formats TBA
 * @property genres TBA
 * @property lengths TBA
 * @property meanScore TBA
 * @property releaseYears TBA
 * @property scores TBA
 * @property staff TBA
 * @property standardDeviation TBA
 * @property startYears TBA
 * @property statuses TBA
 * @property studios TBA
 * @property tags TBA
 * @property voiceActors TBA
 */
interface IUserStatistic {
    val count: Int
    val countries: List<UserCountryStatistic>?
    val formats: List<UserFormatStatistic>?
    val genres: List<UserGenreStatistic>?
    val lengths: List<UserLengthStatistic>?
    val meanScore: Float
    val releaseYears: List<UserReleaseYearStatistic>?
    val scores: List<UserScoreStatistic>?
    val staff: List<UserStaffStatistic>?
    val standardDeviation: Float
    val startYears: List<UserStartYearStatistic>?
    val statuses: List<UserStatusStatistic>?
    val studios: List<UserStudioStatistic>?
    val tags: List<UserTagStatistic>?
    val voiceActors: List<UserVoiceActorStatistic>?
}