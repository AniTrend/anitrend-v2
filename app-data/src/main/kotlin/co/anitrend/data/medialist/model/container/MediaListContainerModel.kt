/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.data.medialist.model.container

import co.anitrend.data.common.model.delete.DeletedModel
import co.anitrend.data.common.model.delete.contract.IDeletedModel
import co.anitrend.data.common.model.paging.data.IPageModel
import co.anitrend.data.common.model.paging.info.PageInfo
import co.anitrend.data.medialist.model.MediaListModel
import co.anitrend.data.user.model.UserModel
import co.anitrend.domain.medialist.enums.MediaListStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal sealed class MediaListContainerModel {

    abstract class Single : MediaListContainerModel() {
        abstract val entry: MediaListModel.Extended
    }

    abstract class Many : MediaListContainerModel() {
        abstract val entries: List<MediaListModel.Extended>
    }

    abstract class Deleted : MediaListContainerModel() {
        abstract val entry: DeletedModel
    }

    @Serializable
    data class Paged(
        @SerialName("Page") val page: Page = Page()
    ) : MediaListContainerModel() {
        @Serializable
        data class Page(
            @SerialName("pageInfo") override val pageInfo: PageInfo? = null,
            @SerialName("mediaList") override val entries: List<MediaListModel.Extended> = emptyList()
        ) : Many(), IPageModel
    }

    /** [MediaListCollection](https://anilist.github.io/ApiV2-GraphQL-Docs/medialistcollection.doc.html)
     * List of anime or manga
     *
     * @param lists Grouped media list entries
     * @param hasNextChunk If there is another chunk
     * @param user The owner of the list
     */
    @Serializable
    data class Collection(
        @SerialName("lists") val lists: List<Group>,
        @SerialName("hasNextChunk") val hasNextChunk: Boolean,
        @SerialName("user") val user: UserModel.Core?
    ) : MediaListContainerModel(){

        /** [MediaListGroup](https://anilist.github.io/ApiV2-GraphQL-Docs/medialistgroup.doc.html)
         * List group of anime or manga entries
         *
         * @param entries Media list entries
         * @param name name of the current group
         * @param isCustomList If the current group is a custom list
         * @param isSplitCompletedList If this grouping is split by types of of media, e.g movies, tv, specials, etc
         * @param status status of current group, one of [MediaListStatus]
         */
        @Serializable
        internal data class Group(
            @SerialName("entries") override val entries: List<MediaListModel.Extended> = emptyList(),
            @SerialName("isCustomList") val isCustomList: Boolean,
            @SerialName("isSplitCompletedList") val isSplitCompletedList: Boolean,
            @SerialName("name") val name: String,
            @SerialName("status") val status: MediaListStatus
        ) : Many()
    }

    @Serializable
    data class Entry(
        @SerialName("MediaList") override val entry: MediaListModel.Extended
    ) : Single()

    @Serializable
    data class SavedEntry(
        @SerialName("SaveMediaListEntry") override val entry: MediaListModel.Extended
    ) : Single()

    @Serializable
    data class SavedEntries(
        @SerialName("UpdateMediaListEntries") override val entries: List<MediaListModel.Extended>
    ) : Many()

    @Serializable
    data class DeletedEntry(
        @SerialName("DeleteMediaListEntry") override val entry: DeletedModel
    ) : Deleted()

    @Serializable
    data class DeletedCustomList(
        @SerialName("DeleteCustomList") override val entry: DeletedModel
    ) : Deleted()
}
