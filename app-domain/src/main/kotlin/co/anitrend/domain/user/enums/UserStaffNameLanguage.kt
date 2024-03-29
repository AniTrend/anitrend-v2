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

package co.anitrend.domain.user.enums

import co.anitrend.domain.common.enums.contract.IAliasable

/**
 * The language the user wants to see staff and character names in
 */
enum class UserStaffNameLanguage(override val alias: CharSequence) : IAliasable {
    /** The staff or character's name in their native language*/
    NATIVE("native"),
    /** The romanization of the staff or character's native name*/
    ROMAJI("Romaji"),
    /** The romanization of the staff or character's native name, with western name ordering*/
    ROMAJI_WESTERN("Romaji Western"),
}