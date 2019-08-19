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

package co.anitrend.data.model.response.core.media

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

/** [MediaTag](https://anilist.github.io/ApiV2-GraphQL-Docs/mediatag.doc.html)
 * A tag that describes a theme or element of the media
 *
 * @param id The id of the tag
 * @param name The name of the tag
 * @param description A general description of the tag
 * @param category The categories of tags this tag belongs to
 * @param rank The relevance ranking of the tag out of the 100 for this media
 * @param isGeneralSpoiler If the tag could be a spoiler for any media
 * @param isMediaSpoiler If the tag is a spoiler for this media
 * @param isAdult If the tag is only for adult 18+ media
 */
@Entity
@Parcelize
data class MediaTag(
    @PrimaryKey
    val id: Int,
    val name: String,
    val description: String?,
    val category: String?,
    val rank: Int?,
    val isGeneralSpoiler: Boolean?,
    val isMediaSpoiler: Boolean?,
    val isAdult: Boolean?
): Parcelable