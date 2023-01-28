/*
 * Copyright (C) 2022  AniTrend
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

package co.anitrend.data.medialist.entity.view

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.medialist.enums.MediaListStatus
import org.intellij.lang.annotations.Language

@DatabaseView(
    value = MediaListCountView.QUERY,
    viewName = "media_list_count"
)
internal data class MediaListCountView(
    @ColumnInfo(name = "list_count") val listCount: Int,
    @ColumnInfo(name = "media_type") val mediaType: MediaType,
    @ColumnInfo(name = "list_status") val listStatus: MediaListStatus,
    @ColumnInfo(name = "user_id") val userId: Long,
) {
    internal companion object {
        @Language("sql")
        const val QUERY = """
            select count(media_list.id) as list_count, media_list.media_type, media_list.list_status, media_list.user_id
            from media_list
            group by media_list.user_id, media_list.media_type, media_list.list_status
        """
    }
}


@DatabaseView(
    value = CustomListCountView.QUERY,
    viewName = "custom_list_count"
)
internal data class CustomListCountView(
    @ColumnInfo(name = "list_count") val listCount: Int,
    @ColumnInfo(name = "media_type") val mediaType: MediaType,
    @ColumnInfo(name = "user_id") val userId: Long,
    @ColumnInfo(name = "list_name") val customListName: String?,
) {
    internal companion object {
        @Language("sql")
        const val QUERY: String = """
            select count(custom_list.id) as list_count, media_list.media_type, custom_list.user_id, custom_list.list_name
            from custom_list
            inner join media_list on media_list.id = custom_list.media_list_id
            where custom_list.enabled = 1
            group by custom_list.user_id, media_list.media_type, custom_list.list_name
        """
    }
}