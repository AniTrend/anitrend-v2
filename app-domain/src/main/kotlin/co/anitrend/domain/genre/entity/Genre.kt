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

package co.anitrend.domain.genre.entity

import co.anitrend.domain.common.HexColor
import co.anitrend.domain.common.entity.contract.IEntity

sealed class Genre : IEntity {

    abstract val name: String
    abstract val decorated: String?

    data class Core(
        override val name: String,
        override val decorated: String?,
        override val id: Long
    ) : Genre()

    data class Extended(
        val background: HexColor?,
        override val name: String,
        override val decorated: String?,
        override val id: Long
    ) : Genre()
}