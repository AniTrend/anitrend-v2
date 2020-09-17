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

package co.anitrend.data.medialist.model.remote

import co.anitrend.data.user.model.remote.UserModelCore
import co.anitrend.domain.medialist.enums.MediaListStatus
import com.google.gson.annotations.SerializedName

/** [MediaListCollection](https://anilist.github.io/ApiV2-GraphQL-Docs/medialistcollection.doc.html)
 * List of anime or manga
 *
 * @param lists Grouped media list entries
 * @param hasNextChunk If there is another chunk
 * @param user The owner of the list
 */
internal data class MediaListCollection(
    @SerializedName("lists") val lists: List<MediaListGroup>,
    @SerializedName("hasNextChunk") val hasNextChunk: Boolean,
    @SerializedName("user") val user: UserModelCore?
) {

    /** [MediaListGroup](https://anilist.github.io/ApiV2-GraphQL-Docs/medialistgroup.doc.html)
     * List group of anime or manga entries
     *
     * @param entries Media list entries
     * @param name name of the current group
     * @param isCustomList If the current group is a custom list
     * @param isSplitCompletedList If this grouping is split by types of of media, e.g movies, tv, specials, etc
     * @param status status of current group, one of [co.anitrend.domain.medialist.enums.MediaListStatus]
     */
    internal data class MediaListGroup(
        @SerializedName("entries") val entries: List<MediaListModelCore>?,
        @SerializedName("isCustomList") val isCustomList: Boolean,
        @SerializedName("isSplitCompletedList") val isSplitCompletedList: Boolean,
        @SerializedName("name") val name: String,
        @SerializedName("status") val status: MediaListStatus
    )
}