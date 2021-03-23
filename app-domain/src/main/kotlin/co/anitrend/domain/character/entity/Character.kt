/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.domain.character.entity

import co.anitrend.domain.character.entity.contract.ICharacter
import co.anitrend.domain.common.entity.shared.CoverImage
import co.anitrend.domain.common.entity.shared.CoverName

sealed class Character : ICharacter {

    data class Core(
        override val description: String?,
        override val image: CoverImage?,
        override val name: CoverName?,
        override val siteUrl: String?,
        override val updatedAt: Long?,
        override val favourites: Int,
        override val isFavourite: Boolean,
        override val id: Long
    ) : Character()
}
