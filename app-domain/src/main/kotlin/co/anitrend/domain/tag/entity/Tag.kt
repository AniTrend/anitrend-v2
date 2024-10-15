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

package co.anitrend.domain.tag.entity

import co.anitrend.domain.common.HexColor
import co.anitrend.domain.common.entity.contract.IEntity

sealed class Tag : IEntity {

    abstract val name: String
    abstract val description: String?
    abstract val category: String?
    abstract val isGeneralSpoiler: Boolean
    abstract val isAdult: Boolean

    data class Core(
        override val name: String,
        override val description: String?,
        override val category: String?,
        override val isGeneralSpoiler: Boolean,
        override val isAdult: Boolean,
        override val id: Long,
    ) : Tag()

    data class Extended(
        val rank: Int,
        val isMediaSpoiler: Boolean,
        val background: HexColor?,
        override val name: String,
        override val description: String?,
        override val category: String?,
        override val isGeneralSpoiler: Boolean,
        override val isAdult: Boolean,
        override val id: Long,
    ) : Tag()

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
