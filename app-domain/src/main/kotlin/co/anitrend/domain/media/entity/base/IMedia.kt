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

package co.anitrend.domain.media.entity.base

import co.anitrend.domain.common.entity.contract.IEntity
import co.anitrend.domain.common.entity.contract.IEntityMediaCover
import co.anitrend.domain.media.entity.attribute.title.IMediaTitle
import co.anitrend.domain.media.entity.contract.MediaCategory

interface IMedia : IEntity {
    val title: IMediaTitle
    val image: IEntityMediaCover
    val category: MediaCategory
    val isAdult: Boolean
    val isFavourite: Boolean
}