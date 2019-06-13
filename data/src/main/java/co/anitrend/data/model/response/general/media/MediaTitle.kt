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

package co.anitrend.data.model.response.general.media

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/** [MediaTitle](https://anilist.github.io/ApiV2-GraphQL-Docs/mediatitle.doc.html)
 * The official titles of the media in various languages
 *
 * @param romaji The romanization of the native language title
 * @param english The official english title
 * @param native Official title in it's native language
 * @param userPreferred The currently authenticated users preferred title language.
 *
 * Default romaji for non-authenticated requests
 */
@Parcelize
data class MediaTitle(
    val romaji: String?,
    val english: String?,
    val native: String?,
    val userPreferred: String?
): Parcelable