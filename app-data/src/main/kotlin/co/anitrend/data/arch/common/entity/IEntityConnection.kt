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

package co.anitrend.data.arch.common.entity

import co.anitrend.domain.common.entity.IEntityPageInfo

/**
 * Connection contract to supply paging information
 *
 * @property edges The connection edge
 * @property nodes The relational nodes
 * @property pageInfo The pagination information
 */
interface IEntityConnection<E, N> {
    val edges: List<E>?
    val nodes: List<N>?
    val pageInfo: IEntityPageInfo?
}