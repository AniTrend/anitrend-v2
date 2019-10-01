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

package co.anitrend.domain.user.enums

/**
 * The language the user wants to see media titles in
 */
enum class UserTitleLanguage {
    /** The official english title */
    ENGLISH,
    /** The official english title, stylised by media creator */
    ENGLISH_STYLISED,
    /** Official title in it's native language */
    NATIVE,
    /** Official title in it's native language, stylised by media creator */
    NATIVE_STYLISED,
    /** The romanization of the native language title */
    ROMAJI,
    /** The romanization of the native language title, stylised by media creator */
    ROMAJI_STYLISED
}