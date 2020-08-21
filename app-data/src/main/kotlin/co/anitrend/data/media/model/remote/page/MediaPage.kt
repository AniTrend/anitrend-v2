/*
 * Copyright (C) 2020  AniTrend
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

package co.anitrend.data.media.model.remote.page

import co.anitrend.data.media.model.remote.MediaModel
import co.anitrend.domain.common.entity.shared.PageInfo
import com.google.gson.annotations.SerializedName

internal data class MediaPage(
    @SerializedName("Page")
    val page: Page
) {
    data class Page(
        @SerializedName("pageInfo")
        val pageInfo: PageInfo,
        @SerializedName("media")
        val media: Collection<MediaModel>
    )
}