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

package co.anitrend.data.repository.staff.attributes

import androidx.annotation.StringDef

/**
 * The primary language of the voice actor
 */
@StringDef(
    StaffLanguageContract.JAPANESE,
    StaffLanguageContract.ENGLISH,
    StaffLanguageContract.KOREAN,
    StaffLanguageContract.ITALIAN,
    StaffLanguageContract.SPANISH,
    StaffLanguageContract.PORTUGUESE,
    StaffLanguageContract.FRENCH,
    StaffLanguageContract.GERMAN,
    StaffLanguageContract.HEBREW,
    StaffLanguageContract.HUNGARIAN
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPEALIAS)
annotation class StaffLanguageContract {
    companion object {
        /** Japanese */
        const val JAPANESE = "JAPANESE"

        /** English */
        const val ENGLISH = "ENGLISH"

        /** Korean */
        const val KOREAN = "KOREAN"

        /** Italian */
        const val ITALIAN = "ITALIAN"

        /** Spanish */
        const val SPANISH = "SPANISH"

        /** Portuguese */
        const val PORTUGUESE = "PORTUGUESE"

        /** French */
        const val FRENCH = "FRENCH"

        /** German */
        const val GERMAN = "GERMAN"

        /** Hebrew */
        const val HEBREW = "HEBREW"

        /** Hungarian */
        const val HUNGARIAN = "HUNGARIAN"
        
        val ALL = listOf(
            JAPANESE,
            ENGLISH,
            KOREAN,
            ITALIAN,
            SPANISH,
            PORTUGUESE,
            FRENCH,
            GERMAN,
            HEBREW,
            HUNGARIAN
        )
    }
}

@StaffLanguageContract
typealias StaffLanguage = String