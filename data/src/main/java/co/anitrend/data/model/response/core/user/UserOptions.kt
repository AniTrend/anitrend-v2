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

package co.anitrend.data.model.response.core.user

import android.os.Parcelable
import co.anitrend.data.usecase.user.attributes.UserTitleLanguage
import kotlinx.android.parcel.Parcelize

/** [UserOptions](https://anilist.github.io/ApiV2-GraphQL-Docs/useroptions.doc.html)
 * A user's general options
 *
 * @param titleLanguage The language the user wants to see media titles in
 * @param displayAdultContent Whether the user has enabled viewing of 18+ content
 * @param airingNotifications Whether the user receives notifications when a show they are watching airs
 * @param profileColor Profile highlight color (blue, purple, pink, orange, red, green, gray)
 */
@Parcelize
data class UserOptions(
    val titleLanguage: UserTitleLanguage,
    val displayAdultContent: Boolean,
    val airingNotifications: Boolean,
    val profileColor: String
): Parcelable