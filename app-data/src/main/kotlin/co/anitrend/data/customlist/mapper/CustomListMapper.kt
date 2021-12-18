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

package co.anitrend.data.customlist.mapper

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.data.android.mapper.EmbedMapper
import co.anitrend.data.customlist.datasource.CustomListLocalSource
import co.anitrend.data.customlist.entity.CustomListEntity
import co.anitrend.data.genre.mapper.GenreMapper
import co.anitrend.data.media.model.MediaModel
import co.anitrend.data.medialist.model.MediaListModel
import co.anitrend.domain.media.enums.MediaType

internal class CustomListMapper(
    override val localSource: CustomListLocalSource
) : EmbedMapper<CustomListMapper.Item, CustomListEntity>()  {

    override val converter = object : SupportConverter<Item, CustomListEntity>() {
        /**
         * Function reference from converting from [M] to [E] which will
         * be called by [convertFrom]
         */
        override val fromType: (Item) -> CustomListEntity = {
            CustomListEntity(
                enabled = it.enabled,
                listName = it.listName,
                mediaListId = it.mediaListId,
                userId = it.userId,
                userName = it.userName
            )
        }

        /**
         * Function reference from converting from [E] to [M] which will
         * be called by [convertTo]
         */
        override val toType: (CustomListEntity) -> Item
            get() = throw NotImplementedError()
    }

    data class Item(
        val enabled: Boolean,
        val listName: String,
        val mediaListId: Long,
        val userId: Long,
        val userName: String,
    )

    companion object {
        fun asItem(source: MediaListModel) =
            source.customLists?.map {
                Item(
                    enabled = it.enabled,
                    listName = it.name,
                    mediaListId = source.id,
                    userId = source.user.id,
                    userName = source.user.name
                )
            }.orEmpty()

        fun asItem(source: List<MediaListModel>) =
            source.flatMap { mediaList ->
                asItem(mediaList)
            }
    }
}