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

package co.anitrend.data.user.model.remote.statistics.media

import co.anitrend.data.user.model.remote.statistics.*
import co.anitrend.data.user.model.remote.statistics.contract.IUserStatistic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** [UserStatistics](https://anilist.github.io/ApiV2-GraphQL-Docs/UserStatistics.doc.html)
 *
 * @param chaptersRead TBA
 * @param volumesRead TBA
 */
@Serializable
internal data class UserMangaStatistics(
    @SerialName("chaptersRead") val chaptersRead: Int,
    @SerialName("volumesRead") val volumesRead: Int,
    @SerialName("count") override val count: Int,
    @SerialName("countries") override val countries: List<UserCountryStatistic>?,
    @SerialName("formats") override val formats: List<UserFormatStatistic>?,
    @SerialName("genres") override val genres: List<UserGenreStatistic>?,
    @SerialName("lengths") override val lengths: List<UserLengthStatistic>?,
    @SerialName("meanScore") override val meanScore: Float,
    @SerialName("releaseYears") override val releaseYears: List<UserReleaseYearStatistic>?,
    @SerialName("scores") override val scores: List<UserScoreStatistic>?,
    @SerialName("staff") override val staff: List<UserStaffStatistic>?,
    @SerialName("standardDeviation") override val standardDeviation: Float,
    @SerialName("startYears") override val startYears: List<UserStartYearStatistic>?,
    @SerialName("statuses") override val statuses: List<UserStatusStatistic>?,
    @SerialName("studios") override val studios: List<UserStudioStatistic>?,
    @SerialName("tags") override val tags: List<UserTagStatistic>?,
    @SerialName("voiceActors") override val voiceActors: List<UserVoiceActorStatistic>?
) : IUserStatistic