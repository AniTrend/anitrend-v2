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

import co.anitrend.data.medialist.model.remote.MediaList
import co.anitrend.domain.medialist.enums.MediaListStatus

/** [MediaListGroup](https://anilist.github.io/ApiV2-GraphQL-Docs/medialistgroup.doc.html)
 * List group of anime or manga entries
 *
 * @param entries Media list entries
 * @param name name of the current group
 * @param isCustomList If the current group is a custom list
 * @param isSplitCompletedList If this grouping is split by types of of media, e.g movies, tv, specials, etc
 * @param status status of current group, one of [co.anitrend.domain.enums.medialist.MediaListStatusContract]
 */
internal data class MediaListGroup(
    val entries: List<MediaList>?,
    val isCustomList: Boolean,
    val isSplitCompletedList: Boolean,
    val name: String,
    val status: MediaListStatus
)