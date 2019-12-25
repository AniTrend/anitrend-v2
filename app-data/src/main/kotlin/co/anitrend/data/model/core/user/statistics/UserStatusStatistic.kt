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

package co.anitrend.data.model.core.user.statistics

import co.anitrend.data.model.core.user.statistics.contract.IStatistic
import co.anitrend.domain.medialist.enums.MediaListStatus

/** [UserStatusStatistic](https://anilist.github.io/ApiV2-GraphQL-Docs/userstatusstatistic.doc.html)
 *
 * @param status media list status
 */
data class UserStatusStatistic(
    val status: MediaListStatus,
    override val chaptersRead: Int,
    override val count: Int,
    override val meanScore: Float,
    override val mediaIds: List<Int>,
    override val minutesWatched: Int
) : IStatistic
