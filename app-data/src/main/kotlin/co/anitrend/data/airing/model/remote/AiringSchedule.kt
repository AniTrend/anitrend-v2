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

package co.anitrend.data.airing.model.remote

import androidx.room.PrimaryKey
import co.anitrend.data.airing.model.remote.contract.IAiringSchedule
import co.anitrend.data.media.model.remote.Media

/** [AiringSchedule](https://anilist.github.io/ApiV2-GraphQL-Docs/airingschedule.doc.html)
 * Media Airing Schedule
 *
 * @param media The associate media of the airing episode
 */
data class AiringSchedule(
    val media: Media?,
    @PrimaryKey
    override val id: Long,
    override val airingAt: Long,
    override val episode: Int,
    override val mediaId: Long,
    override val timeUntilAiring: Long
) : IAiringSchedule