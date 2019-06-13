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

/** [MediaTrailer](https://anilist.github.io/ApiV2-GraphQL-Docs/mediatrailer.doc.html)
 * Media trailer or advertisement
 *
 * @param id The trailer video id
 * @param site The site the video is hosted by (Currently either youtube or dailymotion)
 */
@Parcelize
data class MediaTrailer(
    val id: String?,
    val site: String?
): Parcelable