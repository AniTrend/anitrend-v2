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

package co.anitrend.data.repository.character.attributes

import androidx.annotation.StringDef

/**
 * The role the character plays in the media
 */
@StringDef(
    CharacterRoleContract.MAIN,
    CharacterRoleContract.SUPPORTING,
    CharacterRoleContract.BACKGROUND
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPEALIAS)
annotation class CharacterRoleContract {
    companion object {
        /** A primary character role in the media */
        const val MAIN = "MAIN"

        /** A supporting character role in the media */
        const val SUPPORTING = "SUPPORTING"

        /** A background character in the media */
        const val BACKGROUND = "BACKGROUND"

        val ALL = listOf(
            MAIN,
            SUPPORTING,
            BACKGROUND
        )
    }
}

@CharacterRoleContract
typealias CharacterRole = String