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

package co.anitrend.data.media.model.page

import co.anitrend.data.arch.common.model.paging.info.PageInfo
import co.anitrend.data.media.model.MediaModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class MediaPageModel(
    @SerialName("Page") val page: Page = Page()
) {
    @Serializable
    data class Page(
        @SerialName("pageInfo") val pageInfo: PageInfo? = null,
        @SerialName("media") val media: List<MediaModel.Core> = emptyList()
    )
}