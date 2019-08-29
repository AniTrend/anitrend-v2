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

package co.anitrend.data.model.query.studio.connection

import co.anitrend.data.model.query.paging.contract.IPageQuery
import co.anitrend.data.usecase.media.attributes.MediaSort

/**
 * Studio media connection contract
 *
 * @property isMain If the studio was the primary animation studio of the media
 * @property page The page
 * @property perPage The amount of entries per page, max 25
 * @property sort The order the results will be returned in
 */
interface IStudioMediaConnectionQuery : IPageQuery{
    val isMain: Boolean?
        get() = null
    override var page: Int
    override val perPage: Int
    val sort: List<MediaSort>?
        get() = null
}