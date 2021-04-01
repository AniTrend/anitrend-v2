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

package co.anitrend.data.airing.model.container

import co.anitrend.data.airing.model.AiringScheduleModel
import co.anitrend.data.common.model.paging.info.PageInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal sealed class AiringScheduleModelContainer {

    @Serializable
    data class Paged(
        @SerialName("Page") val page: Page = Page()
    ) : AiringScheduleModelContainer() {

        @Serializable
        data class Page(
            @SerialName("pageInfo") val pageInfo: PageInfo? = null,
            @SerialName("airingSchedules") val airingSchedules: List<AiringScheduleModel.Extended> = emptyList()
        )
    }
}