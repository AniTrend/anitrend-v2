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

package co.anitrend.domain.airing.entity

import co.anitrend.domain.common.entity.contract.IEntity

/**
* @param airingAt The time the episode airs at
* @param episode The airing episode number
* @param mediaId The associate media id of the airing episode
* @param timeUntilAiring Seconds until episode starts airing
* @param id The id of the airing schedule item
*/
data class AiringSchedule(
    val airingAt: UInt,
    val episode: UInt,
    val mediaId: UInt,
    val timeUntilAiring: UInt,
    override val id: Long
) : IEntity