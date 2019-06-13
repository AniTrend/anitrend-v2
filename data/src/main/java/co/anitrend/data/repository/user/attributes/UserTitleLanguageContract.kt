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

package co.anitrend.data.repository.user.attributes

import androidx.annotation.StringDef

/**
 * User title language values
 */
@StringDef(
    UserTitleLanguageContract.ROMAJI,
    UserTitleLanguageContract.ENGLISH,
    UserTitleLanguageContract.NATIVE,
    UserTitleLanguageContract.ROMAJI_STYLISED,
    UserTitleLanguageContract.ENGLISH_STYLISED,
    UserTitleLanguageContract.NATIVE_STYLISED
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPEALIAS)
annotation class UserTitleLanguageContract {
    companion object {
        /** The romanization of the native language title **/
        const val ROMAJI = "ROMAJI"

        /** The official english title **/
        const val ENGLISH = "ENGLISH"

        /** Official title in it's native language **/
        const val NATIVE = "NATIVE"

        /** The romanization of the native language title, stylised by media creator **/
        const val ROMAJI_STYLISED = "ROMAJI_STYLISED"

        /** The official english title, stylised by media creator **/
        const val ENGLISH_STYLISED = "ENGLISH_STYLISED"

        /** Official title in it's native language, stylised by media creator **/
        const val NATIVE_STYLISED = "NATIVE_STYLISED"

        val ALL = listOf(
            ROMAJI,
            ENGLISH,
            NATIVE,
            ROMAJI_STYLISED,
            ENGLISH_STYLISED,
            NATIVE_STYLISED
        )
    }
}

@UserTitleLanguageContract
typealias UserTitleLanguage = String