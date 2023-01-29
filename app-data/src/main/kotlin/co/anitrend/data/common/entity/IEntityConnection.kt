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

package co.anitrend.data.common.entity

import co.anitrend.data.common.entity.IEntityConnection.IEdge
import co.anitrend.data.common.entity.IEntityConnection.INode
import co.anitrend.data.common.model.paging.info.IPageInfo

/**
 * Connection contract to supply paging information
 *
 * @property pageInfo The pagination information
 *
 * @see IEdge
 * @see INode
 */
internal interface IEntityConnection {
    val pageInfo: IPageInfo?

    /**
     * Connection contract
     *
     * @property edges The connection edge
     */
    interface IEdge<E> {
        val edges: List<E>?
    }

    /**
     * Node contract
     *
     * @property nodes The relational nodes
     */
    interface INode<N> {
        val nodes: List<N>?
    }
}