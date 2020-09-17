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

package co.anitrend.domain.character.enums

import co.anitrend.domain.common.enums.contract.IAliasable

/**
 * The role the character plays in the media
 */
enum class CharacterRole(override val alias: CharSequence) : IAliasable {
    /** A primary character role in the media */
    MAIN("Main"),
    /** A supporting character role in the media */
    SUPPORTING("Supporting"),
    /** A background character in the media */
    BACKGROUND("Background")
}