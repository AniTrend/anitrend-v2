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

package co.anitrend.data.model.response.meta

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/** [CoverImage](https://anilist.github.io/ApiV2-GraphQL-Docs/mediacoverimage.doc.html)
 * Shared model between [co.anitrend.data.model.response.general.media.Media] &
 * [co.anitrend.data.model.response.general.user.User]
 *
 * @param large The cover image of media at its largest size
 * @param medium The cover image of media at medium size
 */
@Parcelize
data class CoverImage(
    val large: String?,
    val medium: String?
): Parcelable