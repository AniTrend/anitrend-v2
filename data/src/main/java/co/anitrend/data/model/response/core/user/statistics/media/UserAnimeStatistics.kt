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

package co.anitrend.data.model.response.core.user.statistics.media

import co.anitrend.data.model.response.core.user.statistics.contract.IUserStatistic
import co.anitrend.data.model.response.core.user.statistics.*

/** [UserStatistics](https://anilist.github.io/ApiV2-GraphQL-Docs/UserStatistics.doc.html)
 *
 * @param episodesWatched TBA
 * @param minutesWatched TBA
 */
data class UserAnimeStatistics(
    val minutesWatched: Int,
    val episodesWatched: Int,
    override val count: Int,
    override val countries: List<UserCountryStatistic>?,
    override val formats: List<UserFormatStatistic>?,
    override val genres: List<UserGenreStatistic>?,
    override val lengths: List<UserLengthStatistic>?,
    override val meanScore: Float,
    override val releaseYears: List<UserReleaseYearStatistic>?,
    override val scores: List<UserScoreStatistic>?,
    override val staff: List<UserStaffStatistic>?,
    override val standardDeviation: Float,
    override val startYears: List<UserStartYearStatistic>?,
    override val statuses: List<UserStatusStatistic>?,
    override val studios: List<UserStudioStatistic>?,
    override val tags: List<UserTagStatistic>?,
    override val voiceActors: List<UserVoiceActorStatistic>?
) : IUserStatistic