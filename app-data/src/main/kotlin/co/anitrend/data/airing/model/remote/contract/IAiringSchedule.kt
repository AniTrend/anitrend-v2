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

package co.anitrend.data.airing.model.remote.contract

import co.anitrend.domain.common.entity.IEntity

/** [AiringSchedule](https://anilist.github.io/ApiV2-GraphQL-Docs/airingschedule.doc.html)
 * Media Airing Schedule without media relation object
 *
 * @property airingAt The time the episode airs at
 * @property episode The airing episode number
 * @property mediaId The associate media id of the airing episode
 * @property timeUntilAiring Seconds until episode starts airing
 * @property id The id of the airing schedule item
 */
interface IAiringSchedule : IEntity {
    val airingAt: Long
    val episode: Int
    val mediaId: Long
    val timeUntilAiring: Long
}